package school.sptech.KentoCafe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import school.sptech.KentoCafe.dto.produtoIngrediente.ProdutoIngredienteResponse;
import school.sptech.KentoCafe.entity.ProdutoIngrediente;
import school.sptech.KentoCafe.mapper.ProdutoIngredienteMapper;
import school.sptech.KentoCafe.service.ProdutoIngredienteService;

public class ProdutoIngredienteController {

    final ProdutoIngredienteService produtoIngredienteService;

    public ProdutoIngredienteController(ProdutoIngredienteService produtoIngredienteService) {
        this.produtoIngredienteService = produtoIngredienteService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoIngredienteResponse> buscarProdutoIngredientePorId(
            @RequestParam Integer id
    ){
        ProdutoIngrediente produtoIngrediente = produtoIngredienteService.buscarProdutoIngredientePorId(id);
        if (produtoIngrediente == null){
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).body(ProdutoIngredienteMapper.toResponse(produtoIngrediente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProdutoIngredientePorId(
            @RequestParam Integer id
    ){
    return null;
    }
}
