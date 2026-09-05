package school.sptech.KentoCafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.KentoCafe.entity.Tamanho;
import java.util.Optional;

public interface TamanhoRepository extends JpaRepository<Tamanho, Long> {
    Optional<Tamanho> findByNome(String nome);
}