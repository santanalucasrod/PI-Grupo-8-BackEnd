package school.sptech.KentoCafe.service;


import school.sptech.KentoCafe.entity.ProdutoIngrediente;
import school.sptech.KentoCafe.repository.ProdutoIngredienteRepository;

import java.util.Optional;

public class ProdutoIngredienteService {

    final ProdutoIngredienteRepository produtoIngredienteRepository;

    public ProdutoIngredienteService(ProdutoIngredienteRepository produtoIngredienteRepository) {
        this.produtoIngredienteRepository = produtoIngredienteRepository;
    }

    public ProdutoIngrediente buscarProdutoIngredientePorId(Integer id){
        Optional<ProdutoIngrediente> produtoIngredienteOpt = produtoIngredienteRepository.findById(id);
        if (produtoIngredienteOpt != null){
            return produtoIngredienteOpt.get();
        }
        return null;
    }
}
