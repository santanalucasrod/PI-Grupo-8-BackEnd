package school.sptech.KentoCafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import school.sptech.KentoCafe.entity.Personalizacao;

import java.util.List;


@Repository
public interface PersonalizacaoRepository extends JpaRepository<Personalizacao, Long> {

}
