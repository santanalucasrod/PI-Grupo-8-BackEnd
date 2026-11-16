package school.sptech.KentoCafe.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.KentoCafe.dto.dashboard.DashboardResponse;
import school.sptech.KentoCafe.repository.ItemPedidoRepository;
import school.sptech.KentoCafe.repository.PedidoRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class DashboardService {

    private static final long MAX_DIAS_PERIODO = 366;
    private static final int TOP_PRODUTOS = 10;
    private static final int TOP_PERSONALIZACOES = 8;
    // Só consideramos um volume de pedidos/hora para o limite se ele aconteceu em pelo menos 3 horas,
    // senão uma única hora atípica decidiria o resultado.
    private static final int MIN_HORAS_PARA_LIMITE = 3;
    // O limite é o primeiro volume em que pelo menos 1 em cada 5 pedidos passa da meta de tempo.
    // A mediana sozinha esconde o problema: ela só sobe quando metade dos clientes já está esperando demais.
    private static final double PERCENTUAL_ACIMA_DA_META_TOLERADO = 20.0;
    private static final int TOP_HORARIOS_CRITICOS = 5;

    @Value("${dashboard.meta-minutos:10}")
    private int metaMinutos = 10;

    @Value("${dashboard.hora-abertura:9}")
    private int horaAbertura = 9;

    @Value("${dashboard.hora-fechamento:18}")
    private int horaFechamento = 18;

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public DashboardService(PedidoRepository pedidoRepository,
                            ItemPedidoRepository itemPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    private record PedidoResumo(LocalDateTime feitoEm, LocalDateTime prontoEm, BigDecimal valor, boolean cancelado) {
        Double esperaMinutos() {
            return prontoEm == null ? null : Duration.between(feitoEm, prontoEm).toSeconds() / 60.0;
        }
    }

    public DashboardResponse buscarResumo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Informe 'inicio' e 'fim' no formato yyyy-MM-dd");
        }
        if (dataFim.isBefore(dataInicio)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "A data final precisa ser igual ou posterior à data inicial");
        }
        long dias = ChronoUnit.DAYS.between(dataInicio, dataFim) + 1;
        if (dias > MAX_DIAS_PERIODO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "O período pode ter no máximo " + MAX_DIAS_PERIODO + " dias");
        }

        // período anterior com o mesmo número de dias, terminando na véspera do início
        LocalDate fimAnterior = dataInicio.minusDays(1);
        LocalDate inicioAnterior = fimAnterior.minusDays(dias - 1);

        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(LocalTime.MAX);
        LocalDateTime inicioAnt = inicioAnterior.atStartOfDay();
        LocalDateTime fimAnt = fimAnterior.atTime(LocalTime.MAX);

        List<PedidoResumo> pedidos = buscarPedidos(inicio, fim);
        List<PedidoResumo> pedidosAnteriores = buscarPedidos(inicioAnt, fimAnt);
        List<PedidoResumo> validos = pedidos.stream().filter(p -> !p.cancelado()).toList();

        DashboardResponse resposta = new DashboardResponse();
        resposta.setInicio(dataInicio);
        resposta.setFim(dataFim);
        resposta.setInicioAnterior(inicioAnterior);
        resposta.setFimAnterior(fimAnterior);
        resposta.setMetaMinutos(metaMinutos);
        resposta.setAtual(calcularIndicadores(pedidos, itemPedidoRepository.buscarTotalItensVendidos(inicio, fim)));
        resposta.setAnterior(calcularIndicadores(pedidosAnteriores,
                itemPedidoRepository.buscarTotalItensVendidos(inicioAnt, fimAnt)));
        resposta.setSerieDiaria(montarSerieDiaria(validos, dataInicio, dataFim));
        resposta.setMovimentoPorHora(montarMovimentoPorHora(validos, dataInicio, dataFim));

        Map<LocalDateTime, List<PedidoResumo>> porHora = agruparPorHora(validos);
        List<DashboardResponse.EsperaPorVolume> esperaPorVolume = montarEsperaPorVolume(porHora);
        Integer limite = encontrarLimite(esperaPorVolume);
        resposta.setEsperaPorVolume(esperaPorVolume);
        resposta.setLimitePedidosPorHora(limite);
        resposta.setHorariosCriticos(montarHorariosCriticos(porHora, limite));

        List<DashboardResponse.CategoriaFaturamento> categorias = montarCategorias(inicio, fim);
        BigDecimal faturamentoItens = categorias.stream()
                .map(DashboardResponse.CategoriaFaturamento::getFaturamento)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        resposta.setFaturamentoPorCategoria(categorias);
        resposta.setTopProdutos(montarTopProdutos(inicio, fim, faturamentoItens));
        resposta.setPersonalizacoes(montarPersonalizacoes(inicio, fim));

        return resposta;
    }

    private List<PedidoResumo> buscarPedidos(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepository.buscarPedidosDoPeriodo(inicio, fim).stream()
                .map(linha -> new PedidoResumo(
                        (LocalDateTime) linha[0],
                        (LocalDateTime) linha[1],
                        linha[2] != null ? (BigDecimal) linha[2] : BigDecimal.ZERO,
                        "Cancelado".equals(linha[3])))
                .toList();
    }

    private DashboardResponse.Indicadores calcularIndicadores(List<PedidoResumo> pedidos, Long totalItens) {
        List<PedidoResumo> validos = pedidos.stream().filter(p -> !p.cancelado()).toList();
        BigDecimal faturamento = validos.stream()
                .map(PedidoResumo::valor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long quantidade = validos.size();

        DashboardResponse.Indicadores ind = new DashboardResponse.Indicadores();
        ind.setFaturamento(faturamento);
        ind.setPedidos(quantidade);
        ind.setCancelados(pedidos.size() - quantidade);
        ind.setTicketMedio(quantidade == 0
                ? BigDecimal.ZERO
                : faturamento.divide(BigDecimal.valueOf(quantidade), 2, RoundingMode.HALF_UP));
        ind.setItensPorPedido(quantidade == 0 || totalItens == null
                ? 0.0
                : arredondar((double) totalItens / quantidade, 2));

        List<Double> esperas = validos.stream()
                .map(PedidoResumo::esperaMinutos)
                .filter(e -> e != null)
                .toList();
        if (!esperas.isEmpty()) {
            long noPrazo = esperas.stream().filter(e -> e <= metaMinutos).count();
            ind.setTempoMedianoMinutos(arredondar(mediana(esperas), 1));
            ind.setPercentualNoPrazo(arredondar(100.0 * noPrazo / esperas.size(), 1));
        }
        return ind;
    }

    // Um ponto por dia do período, inclusive os dias sem pedido (aparecem como zero).
    private List<DashboardResponse.PontoSerieDiaria> montarSerieDiaria(List<PedidoResumo> validos,
                                                                       LocalDate inicio, LocalDate fim) {
        Map<LocalDate, List<PedidoResumo>> porDia = new TreeMap<>();
        for (PedidoResumo p : validos) {
            porDia.computeIfAbsent(p.feitoEm().toLocalDate(), d -> new ArrayList<>()).add(p);
        }

        List<DashboardResponse.PontoSerieDiaria> serie = new ArrayList<>();
        for (LocalDate dia = inicio; !dia.isAfter(fim); dia = dia.plusDays(1)) {
            List<PedidoResumo> doDia = porDia.getOrDefault(dia, List.of());
            DashboardResponse.PontoSerieDiaria ponto = new DashboardResponse.PontoSerieDiaria();
            ponto.setData(dia);
            ponto.setTotalPedidos((long) doDia.size());
            ponto.setFaturamento(doDia.stream().map(PedidoResumo::valor).reduce(BigDecimal.ZERO, BigDecimal::add));
            serie.add(ponto);
        }
        return serie;
    }

    // A média divide pelo número de dias úteis (ou de fim de semana) do período, e não só pelos
    // dias que tiveram pedido naquela hora — senão horas fracas pareceriam mais cheias do que são.
    private List<DashboardResponse.MovimentoHora> montarMovimentoPorHora(List<PedidoResumo> validos,
                                                                         LocalDate inicio, LocalDate fim) {
        int diasUteis = 0;
        int diasFimDeSemana = 0;
        for (LocalDate dia = inicio; !dia.isAfter(fim); dia = dia.plusDays(1)) {
            if (isFimDeSemana(dia)) diasFimDeSemana++;
            else diasUteis++;
        }

        int horas = Math.max(0, horaFechamento - horaAbertura);
        long[] totalUteis = new long[horas];
        long[] totalFimDeSemana = new long[horas];
        for (PedidoResumo p : validos) {
            int indice = p.feitoEm().getHour() - horaAbertura;
            if (indice < 0 || indice >= horas) continue;
            if (isFimDeSemana(p.feitoEm().toLocalDate())) totalFimDeSemana[indice]++;
            else totalUteis[indice]++;
        }

        List<DashboardResponse.MovimentoHora> movimento = new ArrayList<>();
        for (int i = 0; i < horas; i++) {
            DashboardResponse.MovimentoHora m = new DashboardResponse.MovimentoHora();
            m.setHora(horaAbertura + i);
            m.setMediaDiasUteis(diasUteis == 0 ? null : arredondar((double) totalUteis[i] / diasUteis, 1));
            m.setMediaFimDeSemana(diasFimDeSemana == 0 ? null : arredondar((double) totalFimDeSemana[i] / diasFimDeSemana, 1));
            movimento.add(m);
        }
        return movimento;
    }

    // Agrupa as horas do período pelo número de pedidos que tiveram e calcula a espera dos pedidos
    // feitos nelas: mostra a partir de qual volume o atendimento começa a atrasar.
    private List<DashboardResponse.EsperaPorVolume> montarEsperaPorVolume(
            Map<LocalDateTime, List<PedidoResumo>> porHora) {
        Map<Integer, List<Double>> esperasPorVolume = new TreeMap<>();
        Map<Integer, Integer> horasPorVolume = new HashMap<>();
        for (List<PedidoResumo> daHora : porHora.values()) {
            int volume = daHora.size();
            horasPorVolume.merge(volume, 1, Integer::sum);
            List<Double> esperas = esperasPorVolume.computeIfAbsent(volume, v -> new ArrayList<>());
            daHora.stream().map(PedidoResumo::esperaMinutos).filter(e -> e != null).forEach(esperas::add);
        }

        List<DashboardResponse.EsperaPorVolume> resultado = new ArrayList<>();
        esperasPorVolume.forEach((volume, esperas) -> {
            if (esperas.isEmpty()) return;
            DashboardResponse.EsperaPorVolume ponto = new DashboardResponse.EsperaPorVolume();
            ponto.setPedidosNaHora(volume);
            ponto.setEsperaMedianaMinutos(arredondar(mediana(esperas), 1));
            long acimaDaMeta = esperas.stream().filter(e -> e > metaMinutos).count();
            ponto.setPercentualAcimaDaMeta(arredondar(100.0 * acimaDaMeta / esperas.size(), 1));
            ponto.setQuantidadeHoras(horasPorVolume.get(volume));
            resultado.add(ponto);
        });
        return resultado;
    }

    private static Map<LocalDateTime, List<PedidoResumo>> agruparPorHora(List<PedidoResumo> validos) {
        Map<LocalDateTime, List<PedidoResumo>> porHora = new HashMap<>();
        for (PedidoResumo p : validos) {
            porHora.computeIfAbsent(p.feitoEm().truncatedTo(ChronoUnit.HOURS), h -> new ArrayList<>()).add(p);
        }
        return porHora;
    }

    // Conta, por dia da semana e hora, quantas vezes uma hora do período chegou ao limite:
    // é onde a dona deve considerar chamar mais um funcionário.
    private List<DashboardResponse.HorarioCritico> montarHorariosCriticos(
            Map<LocalDateTime, List<PedidoResumo>> porHora, Integer limite) {
        if (limite == null) return List.of();

        Map<List<Integer>, Integer> ocorrencias = new HashMap<>();
        porHora.forEach((hora, pedidos) -> {
            if (pedidos.size() >= limite) {
                ocorrencias.merge(List.of(hora.getDayOfWeek().getValue(), hora.getHour()), 1, Integer::sum);
            }
        });

        return ocorrencias.entrySet().stream()
                .map(e -> {
                    DashboardResponse.HorarioCritico h = new DashboardResponse.HorarioCritico();
                    h.setDiaSemana(e.getKey().get(0));
                    h.setHora(e.getKey().get(1));
                    h.setOcorrencias(e.getValue());
                    return h;
                })
                .sorted(Comparator.comparing(DashboardResponse.HorarioCritico::getOcorrencias).reversed()
                        .thenComparing(DashboardResponse.HorarioCritico::getDiaSemana)
                        .thenComparing(DashboardResponse.HorarioCritico::getHora))
                .limit(TOP_HORARIOS_CRITICOS)
                .toList();
    }

    private Integer encontrarLimite(List<DashboardResponse.EsperaPorVolume> esperaPorVolume) {
        return esperaPorVolume.stream()
                .filter(p -> p.getQuantidadeHoras() >= MIN_HORAS_PARA_LIMITE)
                .filter(p -> p.getPercentualAcimaDaMeta() >= PERCENTUAL_ACIMA_DA_META_TOLERADO)
                .map(DashboardResponse.EsperaPorVolume::getPedidosNaHora)
                .findFirst()
                .orElse(null);
    }

    private List<DashboardResponse.CategoriaFaturamento> montarCategorias(LocalDateTime inicio, LocalDateTime fim) {
        List<Object[]> linhas = itemPedidoRepository.buscarFaturamentoPorCategoria(inicio, fim);
        BigDecimal total = linhas.stream()
                .map(l -> (BigDecimal) l[1])
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return linhas.stream().map(linha -> {
            DashboardResponse.CategoriaFaturamento cat = new DashboardResponse.CategoriaFaturamento();
            cat.setCategoria((String) linha[0]);
            cat.setFaturamento((BigDecimal) linha[1]);
            cat.setPercentual(percentual((BigDecimal) linha[1], total));
            return cat;
        }).toList();
    }

    private List<DashboardResponse.ProdutoVendido> montarTopProdutos(LocalDateTime inicio, LocalDateTime fim,
                                                                     BigDecimal faturamentoItens) {
        return itemPedidoRepository.buscarProdutosMaisVendidos(inicio, fim).stream()
                .limit(TOP_PRODUTOS)
                .map(linha -> {
                    DashboardResponse.ProdutoVendido produto = new DashboardResponse.ProdutoVendido();
                    produto.setProduto((String) linha[0]);
                    produto.setQuantidade((Long) linha[1]);
                    produto.setFaturamento((BigDecimal) linha[2]);
                    produto.setPercentualFaturamento(percentual((BigDecimal) linha[2], faturamentoItens));
                    return produto;
                })
                .toList();
    }

    private List<DashboardResponse.PersonalizacaoPedida> montarPersonalizacoes(LocalDateTime inicio, LocalDateTime fim) {
        return itemPedidoRepository.buscarPersonalizacoesMaisPedidas(inicio, fim).stream()
                .limit(TOP_PERSONALIZACOES)
                .map(linha -> {
                    DashboardResponse.PersonalizacaoPedida per = new DashboardResponse.PersonalizacaoPedida();
                    per.setNome((String) linha[0]);
                    per.setQuantidade((Long) linha[1]);
                    return per;
                })
                .toList();
    }

    private static boolean isFimDeSemana(LocalDate dia) {
        return dia.getDayOfWeek() == DayOfWeek.SATURDAY || dia.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private static double mediana(List<Double> valores) {
        List<Double> ordenados = valores.stream().sorted().toList();
        int meio = ordenados.size() / 2;
        return ordenados.size() % 2 == 1
                ? ordenados.get(meio)
                : (ordenados.get(meio - 1) + ordenados.get(meio)) / 2.0;
    }

    private static Double percentual(BigDecimal parte, BigDecimal total) {
        if (parte == null || total == null || total.signum() == 0) return 0.0;
        return arredondar(parte.doubleValue() * 100.0 / total.doubleValue(), 1);
    }

    private static double arredondar(double valor, int casas) {
        return BigDecimal.valueOf(valor).setScale(casas, RoundingMode.HALF_UP).doubleValue();
    }
}
