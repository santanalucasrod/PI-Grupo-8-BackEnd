package school.sptech.KentoCafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import school.sptech.KentoCafe.entity.Produto;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    @Query("SELECT p FROM Produto p JOIN ProdutoIngrediente pi ON p.id = pi.produto.id WHERE pi.ingrediente.id = :ingredienteId")
    List<Produto> findByIngredienteId(Integer ingredienteId);

    List<Produto> findByCategoria_Id(Integer categoriaId);
}