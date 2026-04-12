package school.sptech.KentoCafe.service;

import org.springframework.stereotype.Service;
import school.sptech.KentoCafe.dto.ingrediente.IngredienteRequest;
import school.sptech.KentoCafe.entity.Ingrediente;
import school.sptech.KentoCafe.mapper.IngredienteMapper;
import school.sptech.KentoCafe.repository.IngredienteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class IngredienteService {

    final IngredienteRepository ingredienteRepository;

    public IngredienteService(IngredienteRepository ingredienteRepository) {
        this.ingredienteRepository = ingredienteRepository;
    }

    public List<Ingrediente> buscarIngredientes() {
        return ingredienteRepository.findAll();
    }

    public Ingrediente buscarIngredientePorId(Integer id){
        if (ingredienteRepository.existsById(id)){
            Optional<Ingrediente> ingredienteOpt = ingredienteRepository.findById(id);
            return ingredienteOpt.get();
        }
        return null;
    }

    public Ingrediente criarIngrediente(Ingrediente ingrediente){
        return ingredienteRepository.save(ingrediente);
    }

    public Ingrediente atualizarIngrediente(Integer id, IngredienteRequest req){
        Ingrediente ingredienteAtualizar = IngredienteMapper.toEntity(req);
        ingredienteAtualizar.setId(id);
        return ingredienteRepository.save(ingredienteAtualizar);
    }

    public void deletarPorId(Integer id){
        ingredienteRepository.deleteById(id);
    }

    public List<Ingrediente> buscarIngredientePorProdutoId(Integer id){
        return ingredienteRepository.findByProdutoId(id);
    }
}
