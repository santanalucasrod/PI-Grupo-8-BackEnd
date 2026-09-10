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

    // resumo[0] = faturamento total (BigDecimal), resumo[1] = total de pedidos (Long)
    @Query("""
        SELECT COALESCE(SUM(p.valorTotal), 0), COUNT(p)
        FROM Pedido p
        WHERE p.dtHrPedido BETWEEN :inicio AND :fim
          AND p.status.nome <> 'Cancelado'
        """)
    Object[] buscarResumoFaturamentoEPedidos(@Param("inicio") LocalDateTime inicio,
                                             @Param("fim") LocalDateTime fim);

    // CAST(... AS double) é necessário porque o Hibernate 6.6 não sabe que
    // FUNCTION('TIMESTAMPDIFF', ...) devolve um número — sem o CAST, AVG()
    // rejeita a query na inicialização com FunctionArgumentException.
    @Query("""
        SELECT AVG(CAST(FUNCTION('TIMESTAMPDIFF', MINUTE, p.dtHrPedido, p.dtHrPronto) AS double))
        FROM Pedido p
        WHERE p.dtHrPedido BETWEEN :inicio AND :fim
          AND p.status.nome <> 'Cancelado'
          AND p.dtHrPronto IS NOT NULL
        """)
    Double buscarTempoMedioPreparoMinutos(@Param("inicio") LocalDateTime inicio,
                                          @Param("fim") LocalDateTime fim);

    // cada linha: [0] = data (java.sql.Date), [1] = faturamento do dia (BigDecimal), [2] = total de pedidos do dia (Long)
    @Query("""
        SELECT FUNCTION('DATE', p.dtHrPedido), COALESCE(SUM(p.valorTotal), 0), COUNT(p)
        FROM Pedido p
        WHERE p.dtHrPedido BETWEEN :inicio AND :fim
          AND p.status.nome <> 'Cancelado'
        GROUP BY FUNCTION('DATE', p.dtHrPedido)
        ORDER BY FUNCTION('DATE', p.dtHrPedido)
        """)
    List<Object[]> buscarSerieDiaria(@Param("inicio") LocalDateTime inicio,
                                     @Param("fim") LocalDateTime fim);
}