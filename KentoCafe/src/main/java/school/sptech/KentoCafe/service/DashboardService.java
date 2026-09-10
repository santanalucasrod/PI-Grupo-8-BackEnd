package school.sptech.KentoCafe.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.KentoCafe.dto.dashboard.DashboardResponse;
import school.sptech.KentoCafe.repository.ItemPedidoRepository;
import school.sptech.KentoCafe.repository.PedidoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public DashboardService(PedidoRepository pedidoRepository,
                            ItemPedidoRepository itemPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
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

        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(LocalTime.MAX);

        DashboardResponse resposta = new DashboardResponse();

        Object[] resumo = pedidoRepository.buscarResumoFaturamentoEPedidos(inicio, fim);
        resposta.setFaturamentoTotal(resumo[0] != null ? (BigDecimal) resumo[0] : BigDecimal.ZERO);
        resposta.setTotalPedidos(resumo[1] != null ? (Long) resumo[1] : 0L);

        Double tempoMedio = pedidoRepository.buscarTempoMedioPreparoMinutos(inicio, fim);
        resposta.setTempoMedioPreparoMinutos(
                tempoMedio != null ? Math.round(tempoMedio * 10) / 10.0 : 0.0);

        List<Object[]> produtos = itemPedidoRepository.buscarProdutosMaisVendidos(inicio, fim);
        resposta.setProdutoMaisVendido(produtos.isEmpty() ? null : (String) produtos.get(0)[0]);

        List<DashboardResponse.PontoSerieDiaria> serieDiaria = pedidoRepository
                .buscarSerieDiaria(inicio, fim).stream()
                .map(linha -> {
                    DashboardResponse.PontoSerieDiaria ponto = new DashboardResponse.PontoSerieDiaria();
                    ponto.setData(((java.sql.Date) linha[0]).toLocalDate());
                    ponto.setFaturamento((BigDecimal) linha[1]);
                    ponto.setTotalPedidos((Long) linha[2]);
                    return ponto;
                })
                .collect(Collectors.toList());
        resposta.setSerieDiaria(serieDiaria);

        List<DashboardResponse.CategoriaFaturamento> faturamentoPorCategoria = itemPedidoRepository
                .buscarFaturamentoPorCategoria(inicio, fim).stream()
                .map(linha -> {
                    DashboardResponse.CategoriaFaturamento cat = new DashboardResponse.CategoriaFaturamento();
                    cat.setCategoria((String) linha[0]);
                    cat.setFaturamento((BigDecimal) linha[1]);
                    return cat;
                })
                .collect(Collectors.toList());
        resposta.setFaturamentoPorCategoria(faturamentoPorCategoria);

        return resposta;
    }
}