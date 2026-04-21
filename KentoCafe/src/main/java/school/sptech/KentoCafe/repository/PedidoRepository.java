package school.sptech.KentoCafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.KentoCafe.entity.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
}
