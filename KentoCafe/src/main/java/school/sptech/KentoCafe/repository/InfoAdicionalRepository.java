package school.sptech.KentoCafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.sptech.KentoCafe.entity.InfoAdicional;

@Repository
public interface InfoAdicionalRepository extends JpaRepository<InfoAdicional, Integer> {
    Boolean existsByDescricao(String descricao);
}
