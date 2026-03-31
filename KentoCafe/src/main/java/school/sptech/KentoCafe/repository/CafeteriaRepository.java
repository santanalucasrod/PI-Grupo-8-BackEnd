package school.sptech.KentoCafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.KentoCafe.entity.Funcionario;

public interface CafeteriaRepository extends JpaRepository<Funcionario,Integer> {
}
