package school.sptech.KentoCafe.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import school.sptech.KentoCafe.entity.Ingrediente;

import java.util.List;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {

    @Query("SELECT i FROM Produto p JOIN p.ingredientes i WHERE p.id = :produtoId")
    List<Ingrediente> findIngredientesByProdutoId(@Param("produtoId") Long produtoId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM produto_ingrediente WHERE ingrediente_id = :ingredienteId",
            nativeQuery = true)
    void removerIngredienteDeTodosProdutos(@Param("ingredienteId") Long ingredienteId);
}
