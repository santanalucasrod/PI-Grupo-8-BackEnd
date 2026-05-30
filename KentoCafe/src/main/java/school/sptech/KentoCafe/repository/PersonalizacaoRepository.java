package school.sptech.KentoCafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import school.sptech.KentoCafe.entity.Personalizacao;

import java.util.List;


@Repository
public interface PersonalizacaoRepository extends JpaRepository<Personalizacao, Long> {

    List<Personalizacao> findByTipo(String tipo);

    @Query("SELECT DISTINCT p.tipo FROM Personalizacao p WHERE p.tipo IS NOT NULL")
    List<String> findTiposDisponiveis();
}
