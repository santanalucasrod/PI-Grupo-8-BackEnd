package school.sptech.KentoCafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import school.sptech.KentoCafe.entity.Ingrediente;

import java.util.List;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Integer> {

    @Query("SELECT i FROM Ingrediente i JOIN ProdutoIngrediente pi ON i.id = pi.ingrediente.id WHERE pi.produto.id = :produtoId")
    List<Ingrediente> findByProdutoId(Integer produtoId);
}
