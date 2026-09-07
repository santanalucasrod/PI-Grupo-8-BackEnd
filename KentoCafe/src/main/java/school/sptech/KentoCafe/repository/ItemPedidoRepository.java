package school.sptech.KentoCafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.KentoCafe.entity.ItemPedido;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    boolean existsByProdutoId(Long produtoId);
}
