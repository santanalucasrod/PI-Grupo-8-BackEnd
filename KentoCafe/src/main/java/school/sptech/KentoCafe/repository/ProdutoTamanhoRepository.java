package school.sptech.KentoCafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.KentoCafe.entity.ProdutoTamanho;
import java.util.List;
import java.util.Optional;

public interface ProdutoTamanhoRepository extends JpaRepository<ProdutoTamanho, Long> {
    List<ProdutoTamanho> findByProdutoId(Long produtoId);
    Optional<ProdutoTamanho> findByProdutoIdAndTamanhoId(Long produtoId, Long tamanhoId);
    boolean existsByTamanhoId(Long tamanhoId);
}
