package school.sptech.KentoCafe.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.KentoCafe.dto.dashboard.DashboardResponse;
import school.sptech.KentoCafe.repository.ItemPedidoRepository;
import school.sptech.KentoCafe.repository.PedidoRepository;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Nested
    @DisplayName("Cenários de validação do período informado")
    class ValidacaoTests {

        @Test
        @DisplayName("Deve lançar BAD_REQUEST quando a data de início não for informada (Cenário 1.1)")
        void deveLancarBadRequestQuandoDataInicioForNula() {
            ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                    () -> dashboardService.buscarResumo(null, LocalDate.of(2026, 1, 31)));

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            verifyNoInteractions(pedidoRepository, itemPedidoRepository);
        }

        @Test
        @DisplayName("Deve lançar BAD_REQUEST quando a data de fim não for informada (Cenário 1.2)")
        void deveLancarBadRequestQuandoDataFimForNula() {
            ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                    () -> dashboardService.buscarResumo(LocalDate.of(2026, 1, 1), null));

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            verifyNoInteractions(pedidoRepository, itemPedidoRepository);
        }

        @Test
        @DisplayName("Deve lançar BAD_REQUEST quando a data de fim for anterior à data de início (Cenário 1.3)")
        void deveLancarBadRequestQuandoDataFimForAnteriorADataInicio() {
            LocalDate inicio = LocalDate.of(2026, 2, 10);
            LocalDate fim = LocalDate.of(2026, 2, 1);

            ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                    () -> dashboardService.buscarResumo(inicio, fim));

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertTrue(ex.getReason().contains("data final"));
            verifyNoInteractions(pedidoRepository, itemPedidoRepository);
        }

        @Test
        @DisplayName("Não deve lançar exceção quando a data de início e fim forem iguais (Cenário 1.4)")
        void naoDeveLancarExcecaoQuandoDatasForemIguais() {
            LocalDate data = LocalDate.of(2026, 3, 5);

            when(pedidoRepository.buscarResumoFaturamentoEPedidos(any(), any()))
                    .thenReturn(new Object[]{null, null});
            when(pedidoRepository.buscarTempoMedioPreparoMinutos(any(), any())).thenReturn(null);
            when(itemPedidoRepository.buscarProdutosMaisVendidos(any(), any())).thenReturn(List.of());
            when(pedidoRepository.buscarSerieDiaria(any(), any())).thenReturn(List.of());
            when(itemPedidoRepository.buscarFaturamentoPorCategoria(any(), any())).thenReturn(List.of());

            assertDoesNotThrow(() -> dashboardService.buscarResumo(data, data));
        }
    }

    @Nested
    @DisplayName("Cenários de cálculo do resumo do dashboard")
    class BuscarResumoTests {

        @Test
        @DisplayName("Deve retornar o resumo completo com sucesso quando há dados em todas as métricas (Cenário 2.1)")
        void deveRetornarResumoCompletoComSucesso() {
            LocalDate inicio = LocalDate.of(2026, 1, 1);
            LocalDate fim = LocalDate.of(2026, 1, 31);

            when(pedidoRepository.buscarResumoFaturamentoEPedidos(any(), any()))
                    .thenReturn(new Object[]{BigDecimal.valueOf(1500.50), 10L});
            when(pedidoRepository.buscarTempoMedioPreparoMinutos(any(), any())).thenReturn(12.34);
            when(itemPedidoRepository.buscarProdutosMaisVendidos(any(), any()))
                    .thenReturn(List.of(
                            new Object[]{"Cappuccino", 25L},
                            new Object[]{"Latte", 10L}));
            when(pedidoRepository.buscarSerieDiaria(any(), any()))
                    .thenReturn(List.of(new Object[]{
                            Date.valueOf(LocalDate.of(2026, 1, 5)),
                            BigDecimal.valueOf(500.00),
                            3L
                    }));
            when(itemPedidoRepository.buscarFaturamentoPorCategoria(any(), any()))
                    .thenReturn(List.of(new Object[]{"Bebidas", BigDecimal.valueOf(900.00)}));

            DashboardResponse resultado = dashboardService.buscarResumo(inicio, fim);

            assertNotNull(resultado);
            assertEquals(0, BigDecimal.valueOf(1500.50).compareTo(resultado.getFaturamentoTotal()));
            assertEquals(10L, resultado.getTotalPedidos());
            assertEquals(12.3, resultado.getTempoMedioPreparoMinutos(), 0.0001);
            assertEquals("Cappuccino", resultado.getProdutoMaisVendido());

            assertEquals(1, resultado.getSerieDiaria().size());
            assertEquals(LocalDate.of(2026, 1, 5), resultado.getSerieDiaria().get(0).getData());
            assertEquals(0, BigDecimal.valueOf(500.00).compareTo(resultado.getSerieDiaria().get(0).getFaturamento()));
            assertEquals(3L, resultado.getSerieDiaria().get(0).getTotalPedidos());

            assertEquals(1, resultado.getFaturamentoPorCategoria().size());
            assertEquals("Bebidas", resultado.getFaturamentoPorCategoria().get(0).getCategoria());
            assertEquals(0, BigDecimal.valueOf(900.00).compareTo(resultado.getFaturamentoPorCategoria().get(0).getFaturamento()));
        }

        @Test
        @DisplayName("Deve retornar valores padrão (zero) quando não houver faturamento nem pedidos no período (Cenário 2.2)")
        void deveRetornarValoresPadraoQuandoNaoHouverDados() {
            LocalDate inicio = LocalDate.of(2026, 1, 1);
            LocalDate fim = LocalDate.of(2026, 1, 31);

            when(pedidoRepository.buscarResumoFaturamentoEPedidos(any(), any()))
                    .thenReturn(new Object[]{null, null});
            when(pedidoRepository.buscarTempoMedioPreparoMinutos(any(), any())).thenReturn(null);
            when(itemPedidoRepository.buscarProdutosMaisVendidos(any(), any())).thenReturn(List.of());
            when(pedidoRepository.buscarSerieDiaria(any(), any())).thenReturn(List.of());
            when(itemPedidoRepository.buscarFaturamentoPorCategoria(any(), any())).thenReturn(List.of());

            DashboardResponse resultado = dashboardService.buscarResumo(inicio, fim);

            assertNotNull(resultado);
            assertEquals(0, BigDecimal.ZERO.compareTo(resultado.getFaturamentoTotal()));
            assertEquals(0L, resultado.getTotalPedidos());
            assertEquals(0.0, resultado.getTempoMedioPreparoMinutos(), 0.0001);
            assertNull(resultado.getProdutoMaisVendido());
            assertTrue(resultado.getSerieDiaria().isEmpty());
            assertTrue(resultado.getFaturamentoPorCategoria().isEmpty());
        }

        @Test
        @DisplayName("Deve retornar produtoMaisVendido nulo quando não houver produtos vendidos no período (Cenário 2.3)")
        void deveRetornarProdutoMaisVendidoNuloQuandoListaVazia() {
            LocalDate inicio = LocalDate.of(2026, 1, 1);
            LocalDate fim = LocalDate.of(2026, 1, 31);

            when(pedidoRepository.buscarResumoFaturamentoEPedidos(any(), any()))
                    .thenReturn(new Object[]{BigDecimal.TEN, 1L});
            when(pedidoRepository.buscarTempoMedioPreparoMinutos(any(), any())).thenReturn(5.0);
            when(itemPedidoRepository.buscarProdutosMaisVendidos(any(), any())).thenReturn(List.of());
            when(pedidoRepository.buscarSerieDiaria(any(), any())).thenReturn(List.of());
            when(itemPedidoRepository.buscarFaturamentoPorCategoria(any(), any())).thenReturn(List.of());

            DashboardResponse resultado = dashboardService.buscarResumo(inicio, fim);

            assertNull(resultado.getProdutoMaisVendido());
        }

        @Test
        @DisplayName("Deve arredondar o tempo médio de preparo para uma casa decimal (Cenário 2.4)")
        void deveArredondarTempoMedioDePreparo() {
            LocalDate inicio = LocalDate.of(2026, 1, 1);
            LocalDate fim = LocalDate.of(2026, 1, 31);

            when(pedidoRepository.buscarResumoFaturamentoEPedidos(any(), any()))
                    .thenReturn(new Object[]{BigDecimal.ZERO, 0L});
            when(pedidoRepository.buscarTempoMedioPreparoMinutos(any(), any())).thenReturn(15.15);
            when(itemPedidoRepository.buscarProdutosMaisVendidos(any(), any())).thenReturn(List.of());
            when(pedidoRepository.buscarSerieDiaria(any(), any())).thenReturn(List.of());
            when(itemPedidoRepository.buscarFaturamentoPorCategoria(any(), any())).thenReturn(List.of());

            DashboardResponse resultado = dashboardService.buscarResumo(inicio, fim);

            assertEquals(Math.round(15.15 * 10) / 10.0, resultado.getTempoMedioPreparoMinutos(), 0.0001);
        }

        @Test
        @DisplayName("Deve consultar os repositórios usando o início e o fim exatos do período informado (Cenário 2.5)")
        void deveConsultarRepositoriosComIntervaloCorreto() {
            LocalDate inicio = LocalDate.of(2026, 5, 10);
            LocalDate fim = LocalDate.of(2026, 5, 20);

            when(pedidoRepository.buscarResumoFaturamentoEPedidos(any(), any()))
                    .thenReturn(new Object[]{BigDecimal.ZERO, 0L});
            when(pedidoRepository.buscarTempoMedioPreparoMinutos(any(), any())).thenReturn(0.0);
            when(itemPedidoRepository.buscarProdutosMaisVendidos(any(), any())).thenReturn(List.of());
            when(pedidoRepository.buscarSerieDiaria(any(), any())).thenReturn(List.of());
            when(itemPedidoRepository.buscarFaturamentoPorCategoria(any(), any())).thenReturn(List.of());

            dashboardService.buscarResumo(inicio, fim);

            ArgumentCaptor<LocalDateTime> inicioCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
            ArgumentCaptor<LocalDateTime> fimCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

            verify(pedidoRepository).buscarResumoFaturamentoEPedidos(inicioCaptor.capture(), fimCaptor.capture());

            assertEquals(inicio.atStartOfDay(), inicioCaptor.getValue());
            assertEquals(fim.atTime(LocalTime.MAX), fimCaptor.getValue());

            verify(pedidoRepository, times(1)).buscarTempoMedioPreparoMinutos(inicio.atStartOfDay(), fim.atTime(LocalTime.MAX));
            verify(itemPedidoRepository, times(1)).buscarProdutosMaisVendidos(inicio.atStartOfDay(), fim.atTime(LocalTime.MAX));
            verify(pedidoRepository, times(1)).buscarSerieDiaria(inicio.atStartOfDay(), fim.atTime(LocalTime.MAX));
            verify(itemPedidoRepository, times(1)).buscarFaturamentoPorCategoria(inicio.atStartOfDay(), fim.atTime(LocalTime.MAX));
        }
    }
}
