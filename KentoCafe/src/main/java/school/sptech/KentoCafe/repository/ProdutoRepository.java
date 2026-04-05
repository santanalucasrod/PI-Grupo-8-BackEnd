package school.sptech.KentoCafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.sptech.KentoCafe.entity.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}