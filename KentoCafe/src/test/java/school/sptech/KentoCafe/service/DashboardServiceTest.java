package school.sptech.KentoCafe.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.KentoCafe.dto.dashboard.DashboardResponse;
import school.sptech.KentoCafe.repository.ItemPedidoRepository;
import school.sptech.KentoCafe.repository.PedidoRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DashboardServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @InjectMocks
    private DashboardService dashboardService;

    // 05/01/2026 é segunda-feira; 10/01/2026 é sábado
    private static final LocalDate SEGUNDA = LocalDate.of(2026, 1, 5);
    private static final LocalDate SABADO = LocalDate.of(2026, 1, 10);

    private static Object[] pedido(LocalDate dia, int hora, int minuto, Integer minutosAtePronto,
                                   String valor, String status) {
        LocalDateTime feito = dia.atTime(hora, minuto);
        return new Object[]{
                feito,
                minutosAtePronto == null ? null : feito.plusMinutes(minutosAtePronto),
                new BigDecimal(valor),
                status
        };
    }

    @BeforeEach
    void semDadosPorPadrao() {
        when(pedidoRepository.buscarPedidosDoPeriodo(any(), any())).thenReturn(List.of());
        when(itemPedidoRepository.buscarTotalItensVendidos(any(), any())).thenReturn(0L);
        when(itemPedidoRepository.buscarFaturamentoPorCategoria(any(), any())).thenReturn(List.of());
        when(itemPedidoRepository.buscarProdutosMaisVendidos(any(), any())).thenReturn(List.of());
        when(itemPedidoRepository.buscarPersonalizacoesMaisPedidas(any(), any())).thenReturn(List.of());
    }

    @Nested
    @DisplayName("Cenários de validação do período informado")
    class ValidacaoTests {

        @Test
        @DisplayName("Deve lançar BAD_REQUEST quando a data de início não for informada")
        void deveLancarBadRequestQuandoDataInicioForNula() {
            ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                    () -> dashboardService.buscarResumo(null, LocalDate.of(2026, 1, 31)));

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            verifyNoInteractions(pedidoRepository, itemPedidoRepository);
        }

        @Test
        @DisplayName("Deve lançar BAD_REQUEST quando a data de fim não for informada")
        void deveLancarBadRequestQuandoDataFimForNula() {
            ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                    () -> dashboardService.buscarResumo(LocalDate.of(2026, 1, 1), null));

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            verifyNoInteractions(pedidoRepository, itemPedidoRepository);
        }

        @Test
        @DisplayName("Deve lançar BAD_REQUEST quando a data de fim for anterior à data de início")
        void deveLancarBadRequestQuandoDataFimForAnteriorADataInicio() {
            ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                    () -> dashboardService.buscarResumo(LocalDate.of(2026, 2, 10), LocalDate.of(2026, 2, 1)));

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertTrue(ex.getReason().contains("data final"));
            verifyNoInteractions(pedidoRepository, itemPedidoRepository);
        }

        @Test
        @DisplayName("Deve lançar BAD_REQUEST quando o período passar de 366 dias")
        void deveLancarBadRequestQuandoPeriodoForMuitoLongo() {
            ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                    () -> dashboardService.buscarResumo(LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 2)));

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            verifyNoInteractions(pedidoRepository, itemPedidoRepository);
        }

        @Test
        @DisplayName("Deve consultar o período anterior com o mesmo número de dias")
        void deveConsultarPeriodoAnteriorDeMesmoTamanho() {
            DashboardResponse resultado = dashboardService.buscarResumo(
                    LocalDate.of(2026, 3, 8), LocalDate.of(2026, 3, 14));

            assertEquals(LocalDate.of(2026, 3, 1), resultado.getInicioAnterior());
            assertEquals(LocalDate.of(2026, 3, 7), resultado.getFimAnterior());
            verify(pedidoRepository).buscarPedidosDoPeriodo(
                    eq(LocalDate.of(2026, 3, 1).atStartOfDay()), eq(LocalDate.of(2026, 3, 7).atTime(LocalTime.MAX)));
        }
    }

    @Nested
    @DisplayName("Cenários de cálculo dos indicadores")
    class IndicadoresTests {

        @Test
        @DisplayName("Deve devolver zeros e listas vazias quando não há pedidos")
        void deveRetornarZerosSemPedidos() {
            DashboardResponse resultado = dashboardService.buscarResumo(SEGUNDA, SEGUNDA);

            assertEquals(BigDecimal.ZERO, resultado.getAtual().getFaturamento());
            assertEquals(0L, resultado.getAtual().getPedidos());
            assertEquals(BigDecimal.ZERO, resultado.getAtual().getTicketMedio());
            assertNull(resultado.getAtual().getTempoMedianoMinutos());
            assertNull(resultado.getAtual().getPercentualNoPrazo());
            assertNull(resultado.getLimitePedidosPorHora());
            assertTrue(resultado.getHorariosCriticos().isEmpty());
            assertEquals(1, resultado.getSerieDiaria().size());
            assertEquals(0L, resultado.getSerieDiaria().getFirst().getTotalPedidos());
            assertTrue(resultado.getTopProdutos().isEmpty());
        }

        @Test
        @DisplayName("Deve ignorar cancelados no faturamento e calcular ticket, mediana e % no prazo")
        void deveCalcularIndicadoresDoPeriodo() {
            when(pedidoRepository.buscarPedidosDoPeriodo(eq(SEGUNDA.atStartOfDay()), any()))
                    .thenReturn(List.of(
                            pedido(SEGUNDA, 9, 0, 4, "20.00", "Pronto"),
                            pedido(SEGUNDA, 9, 10, 8, "30.00", "Pronto"),
                            pedido(SEGUNDA, 9, 20, 15, "10.00", "Pronto"),
                            pedido(SEGUNDA, 9, 30, null, "50.00", "Cancelado")));
            when(itemPedidoRepository.buscarTotalItensVendidos(eq(SEGUNDA.atStartOfDay()), any())).thenReturn(5L);

            DashboardResponse.Indicadores atual = dashboardService.buscarResumo(SEGUNDA, SEGUNDA).getAtual();

            assertEquals(new BigDecimal("60.00"), atual.getFaturamento());
            assertEquals(3L, atual.getPedidos());
            assertEquals(1L, atual.getCancelados());
            assertEquals(new BigDecimal("20.00"), atual.getTicketMedio());
            assertEquals(1.67, atual.getItensPorPedido());
            assertEquals(8.0, atual.getTempoMedianoMinutos());
            assertEquals(66.7, atual.getPercentualNoPrazo());
        }

        @Test
        @DisplayName("Deve calcular a média por hora separando dias úteis e fim de semana")
        void deveCalcularMovimentoPorHora() {
            // período de segunda (05/01) a domingo (11/01): 5 dias úteis e 2 de fim de semana
            when(pedidoRepository.buscarPedidosDoPeriodo(eq(SEGUNDA.atStartOfDay()), any()))
                    .thenReturn(List.of(
                            pedido(SEGUNDA, 9, 0, 3, "10.00", "Pronto"),
                            pedido(SEGUNDA, 9, 30, 3, "10.00", "Pronto"),
                            pedido(SEGUNDA.plusDays(1), 9, 15, 3, "10.00", "Pronto"),
                            pedido(SABADO, 9, 5, 3, "10.00", "Pronto"),
                            pedido(SABADO, 14, 0, 3, "10.00", "Pronto")));

            List<DashboardResponse.MovimentoHora> movimento =
                    dashboardService.buscarResumo(SEGUNDA, SEGUNDA.plusDays(6)).getMovimentoPorHora();

            assertEquals(9, movimento.size()); // 9h às 17h
            DashboardResponse.MovimentoHora noveHoras = movimento.getFirst();
            assertEquals(9, noveHoras.getHora());
            assertEquals(0.6, noveHoras.getMediaDiasUteis());   // 3 pedidos / 5 dias úteis
            assertEquals(0.5, noveHoras.getMediaFimDeSemana()); // 1 pedido / 2 dias
            assertEquals(0.5, movimento.get(5).getMediaFimDeSemana()); // 14h
        }

        @Test
        @DisplayName("Deve apontar o volume de pedidos por hora em que a espera passa da meta")
        void deveEncontrarLimiteDePedidosPorHora() {
            List<Object[]> pedidos = new ArrayList<>();
            // 3 horas com 2 pedidos rápidos (5 min) e 3 horas com 4 pedidos lentos (12 min)
            for (int dia = 0; dia < 3; dia++) {
                LocalDate data = SEGUNDA.plusDays(dia);
                for (int i = 0; i < 2; i++) pedidos.add(pedido(data, 9, i * 10, 5, "10.00", "Pronto"));
                for (int i = 0; i < 4; i++) pedidos.add(pedido(data, 13, i * 10, 12, "10.00", "Pronto"));
            }
            when(pedidoRepository.buscarPedidosDoPeriodo(eq(SEGUNDA.atStartOfDay()), any())).thenReturn(pedidos);

            DashboardResponse resultado = dashboardService.buscarResumo(SEGUNDA, SEGUNDA.plusDays(2));

            assertEquals(4, resultado.getLimitePedidosPorHora());
            assertEquals(2, resultado.getEsperaPorVolume().size());
            assertEquals(5.0, resultado.getEsperaPorVolume().get(0).getEsperaMedianaMinutos());
            assertEquals(0.0, resultado.getEsperaPorVolume().get(0).getPercentualAcimaDaMeta());
            assertEquals(100.0, resultado.getEsperaPorVolume().get(1).getPercentualAcimaDaMeta());
            // as horas de 13h de segunda, terça e quarta chegaram ao limite
            assertEquals(3, resultado.getHorariosCriticos().size());
            assertEquals(1, resultado.getHorariosCriticos().getFirst().getDiaSemana());
            assertEquals(13, resultado.getHorariosCriticos().getFirst().getHora());
            assertEquals(3, resultado.getEsperaPorVolume().get(1).getQuantidadeHoras());
        }

        @Test
        @DisplayName("Deve calcular o percentual de faturamento de produtos e categorias")
        void deveCalcularPercentuaisDeProdutosECategorias() {
            when(itemPedidoRepository.buscarFaturamentoPorCategoria(any(), any()))
                    .thenReturn(List.of(
                            new Object[]{"Bebidas Quentes", new BigDecimal("300.00")},
                            new Object[]{"Doces", new BigDecimal("100.00")}));
            when(itemPedidoRepository.buscarProdutosMaisVendidos(any(), any()))
                    .thenReturn(List.<Object[]>of(new Object[]{"Cappuccino", 20L, new BigDecimal("100.00")}));
            when(itemPedidoRepository.buscarPersonalizacoesMaisPedidas(any(), any()))
                    .thenReturn(List.<Object[]>of(new Object[]{"Leite de aveia", 7L}));

            DashboardResponse resultado = dashboardService.buscarResumo(SEGUNDA, SEGUNDA);

            assertEquals(75.0, resultado.getFaturamentoPorCategoria().get(0).getPercentual());
            assertEquals(25.0, resultado.getTopProdutos().getFirst().getPercentualFaturamento());
            assertEquals(20L, resultado.getTopProdutos().getFirst().getQuantidade());
            assertEquals("Leite de aveia", resultado.getPersonalizacoes().getFirst().getNome());
        }
    }
}
