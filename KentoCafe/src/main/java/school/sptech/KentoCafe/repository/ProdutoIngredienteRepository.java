package school.sptech.KentoCafe.repository;

import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.entity.ProdutoIngrediente;

import java.util.List;

public interface ProdutoIngredienteRepository extends JpaRepository<ProdutoIngrediente, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM ProdutoIngrediente pi WHERE pi.ingrediente.id = :ingredienteId")
    void deletarProdutoIngredientePorIngredienteId(@Param("ingredienteId") Integer ingredienteId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProdutoIngrediente pi WHERE pi.produto.id = :produtoId")
    void deletarProdutoIngredientePorProdutoId(@Param("produtoId") Integer produtoId);

    @Query("SELECT count(pi) FROM ProdutoIngrediente pi WHERE pi.ingrediente.id = :ingredienteId")
    Integer existsByIngredienteId(Integer ingredienteId);

    @Query("SELECT count(pi) FROM ProdutoIngrediente pi WHERE pi.produto.id = :produtoId")
    Integer existsByProdutoId(Integer produtoId);

    @Query("SELECT pi FROM ProdutoIngrediente pi WHERE pi.ingrediente.id = :ingredienteId")
    List<ProdutoIngrediente> findByIngredienteId(Integer ingredienteId);

    @Query("SELECT pi FROM ProdutoIngrediente pi WHERE pi.produto.id = :produtoId")
    List<ProdutoIngrediente> findByProdutoId(Integer produtoId);


}
