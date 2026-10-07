package school.sptech.KentoCafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import school.sptech.KentoCafe.entity.Pedido;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByStatusNome(String statusNome);

    List<Pedido> findByStatusNomeIn(List<String> statusNomes);

    List<Pedido> findByFuncionarioId(Integer funcionarioId);

    // ── Dashboard ──────────────────────────────────────────────────────────

    // cada linha: [0] = dtHrPedido (LocalDateTime), [1] = dtHrPronto (LocalDateTime, pode ser null),
    // [2] = valorTotal (BigDecimal), [3] = nome do status (String).
    // Os indicadores (mediana, médias por hora etc.) são calculados no DashboardService.
    @Query("""
        SELECT p.dtHrPedido, p.dtHrPronto, p.valorTotal, p.status.nome
        FROM Pedido p
        WHERE p.dtHrPedido BETWEEN :inicio AND :fim
        """)
    List<Object[]> buscarPedidosDoPeriodo(@Param("inicio") LocalDateTime inicio,
                                          @Param("fim") LocalDateTime fim);
}
