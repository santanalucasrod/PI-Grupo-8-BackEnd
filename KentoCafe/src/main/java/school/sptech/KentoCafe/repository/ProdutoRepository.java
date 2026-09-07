package school.sptech.KentoCafe.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import school.sptech.KentoCafe.entity.Produto;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByCategoriaId(Long id);

    @Query("SELECT p FROM Produto p JOIN p.ingredientes i WHERE i.id = :ingredienteId")
    List<Produto> findByIngredienteId(@Param("ingredienteId") Long ingredienteId);

    @Query("SELECT COUNT(p) FROM Produto p JOIN p.ingredientes i WHERE i.id = :ingredienteId")
    Integer contarProdutosPorIngrediente(@Param("ingredienteId") Long ingredienteId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM produto_ingrediente WHERE produto_id = :produtoId",
            nativeQuery = true)
    void removerTodosIngredientesDoProduto(@Param("produtoId") Long produtoId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM produto_ingrediente WHERE produto_id = :produtoId AND ingrediente_id = :ingredienteId",
            nativeQuery = true)
    void removerIngredienteDoProduto(@Param("produtoId") Long produtoId,
                                     @Param("ingredienteId") Long ingredienteId);

    List<Produto> findByAtivoTrue();
    List<Produto> findByCategoriaIdAndAtivoTrue(Long categoriaId);
}