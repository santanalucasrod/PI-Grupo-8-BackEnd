package school.sptech.KentoCafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.sptech.KentoCafe.entity.Pedido;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByStatusNome(String statusNome);

    List<Pedido> findByFuncionarioId(Integer funcionarioId);
}
