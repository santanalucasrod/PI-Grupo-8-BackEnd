package school.sptech.KentoCafe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.KentoCafe.dto.produtoIngrediente.ProdutoIngredienteRequest;
import school.sptech.KentoCafe.dto.produtoIngrediente.ProdutoIngredienteResponse;
import school.sptech.KentoCafe.entity.ProdutoIngrediente;
import school.sptech.KentoCafe.mapper.ProdutoIngredienteMapper;
import school.sptech.KentoCafe.service.ProdutoIngredienteService;

import java.util.List;

@RestController
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
        if (produtoIngredienteService.buscarProdutoIngredientePorId(id) == null){
            return ResponseEntity.status(404).build();
        }
        produtoIngredienteService.excluirPorId(id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/por-ingrediente/{id}")
    public ResponseEntity<List<ProdutoIngredienteResponse>> buscarProdutoIngredientePorIngredienteId(
            @RequestParam Integer id
    ){
        List<ProdutoIngrediente> produtoIngredientes = produtoIngredienteService.buscarProdutoIngredientePorIngredienteId(id);
        if (produtoIngredientes.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(ProdutoIngredienteMapper.toResponseList(produtoIngredientes));
    }

    @GetMapping("/por-produto/{id}")
    public ResponseEntity<List<ProdutoIngredienteResponse>> buscarProdutoIngredientePorProdutoId(
            @RequestParam Integer id
    ){
        List<ProdutoIngrediente> produtoIngredientes = produtoIngredienteService.buscarProdutoIngredientePorProdutoId(id);
        if (produtoIngredientes.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(ProdutoIngredienteMapper.toResponseList(produtoIngredientes));
    }

    @DeleteMapping("/por-produto/{id}")
    public ResponseEntity<Void> deletarProdutoIngredientePorProdutoId(
            @RequestParam Integer id
    ){
        if (produtoIngredienteService.existeProdutoIngredientePorProdutoId(id)){
            produtoIngredienteService.deleteProdutoIngredientePorProdutoId(id);
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(404).build();
    }

    @DeleteMapping("/por-ingrediente/{id}")
    public ResponseEntity<Void> deletarProdutoIngredientePorIngredienteId(
            @RequestParam Integer id
    ){
        if (produtoIngredienteService.existeProdutoIngredientePorIngredienteId(id)){
            produtoIngredienteService.deleteProdutoIngredientePorIngredienteId(id);
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(404).build();
    }

    @GetMapping
    public ResponseEntity<List<ProdutoIngredienteResponse>> buscarProdutoIngredientes(){
        List<ProdutoIngrediente> produtoIngredientes = produtoIngredienteService.buscarProdutoIngredientes();
        if (produtoIngredientes.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(ProdutoIngredienteMapper.toResponseList(produtoIngredientes));
    }

    @PostMapping
    public ResponseEntity<ProdutoIngredienteResponse> criarProdutoIngrediente(
            @RequestBody ProdutoIngredienteRequest req
    ){
        ProdutoIngrediente produtoIngrediente = produtoIngredienteService.criarProdutoIngrediente(req);
        return ResponseEntity.status(201).body(ProdutoIngredienteMapper.toResponse(produtoIngrediente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoIngredienteResponse> editarProdutoIngrediente(
            @RequestParam Integer id,
            @RequestBody ProdutoIngredienteRequest req
    ){
        if (buscarProdutoIngredientePorId(id) != null){
            ProdutoIngrediente produtoIngrediente = produtoIngredienteService.editarProdutoIngrediente(id, req);
            return ResponseEntity.status(200).body(ProdutoIngredienteMapper.toResponse(produtoIngrediente));
        }
        return ResponseEntity.status(404).build();
    }
}
