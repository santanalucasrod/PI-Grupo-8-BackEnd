package school.sptech.KentoCafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.KentoCafe.entity.ProdutoIngrediente;

public interface ProdutoIngredienteRepository extends JpaRepository<ProdutoIngrediente, Integer> {
}
