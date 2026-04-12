package school.sptech.KentoCafe.service;


import org.springframework.stereotype.Service;
import school.sptech.KentoCafe.entity.ProdutoIngrediente;
import school.sptech.KentoCafe.repository.ProdutoIngredienteRepository;

import java.util.Optional;

@Service
public class ProdutoIngredienteService {

    final ProdutoIngredienteRepository produtoIngredienteRepository;

    public ProdutoIngredienteService(ProdutoIngredienteRepository produtoIngredienteRepository) {
        this.produtoIngredienteRepository = produtoIngredienteRepository;
    }

    public ProdutoIngrediente buscarProdutoIngredientePorId(Integer id){
        if (produtoIngredienteRepository.existsById(id)){
            Optional<ProdutoIngrediente> produtoIngredienteOpt = produtoIngredienteRepository.findById(id);
            return produtoIngredienteOpt.get();
        }
        return null;
    }

    public void excluirPorId(Integer id) {
        produtoIngredienteRepository.deleteById(id);
    }
}
