package school.sptech.KentoCafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import school.sptech.KentoCafe.entity.ItemPedido;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    boolean existsByProdutoId(Long produtoId);

    // ── Dashboard ──────────────────────────────────────────────────────────

    // cada linha: [0] = nome do produto (String), [1] = quantidade total vendida (Long)
    // ordenado do mais vendido para o menos vendido; pegue o item 0 da lista pro "produto mais vendido"
    @Query("""
        SELECT ip.produto.nome, SUM(ip.quantidade) AS totalVendido
        FROM ItemPedido ip
        WHERE ip.pedido.dtHrPedido BETWEEN :inicio AND :fim
          AND ip.pedido.status.nome <> 'Cancelado'
        GROUP BY ip.produto.nome
        ORDER BY totalVendido DESC
        """)
    List<Object[]> buscarProdutosMaisVendidos(@Param("inicio") LocalDateTime inicio,
                                              @Param("fim") LocalDateTime fim);

    // cada linha: [0] = nome da categoria (String), [1] = faturamento da categoria (BigDecimal)
    @Query("""
        SELECT ip.produto.categoria.nome, SUM(ip.precoUnidade * ip.quantidade)
        FROM ItemPedido ip
        WHERE ip.pedido.dtHrPedido BETWEEN :inicio AND :fim
          AND ip.pedido.status.nome <> 'Cancelado'
        GROUP BY ip.produto.categoria.nome
        ORDER BY SUM(ip.precoUnidade * ip.quantidade) DESC
        """)
    List<Object[]> buscarFaturamentoPorCategoria(@Param("inicio") LocalDateTime inicio,
                                                 @Param("fim") LocalDateTime fim);
}