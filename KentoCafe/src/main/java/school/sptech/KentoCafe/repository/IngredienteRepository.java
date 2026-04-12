package school.sptech.KentoCafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.KentoCafe.entity.Ingrediente;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Integer> {
}
