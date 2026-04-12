package school.sptech.KentoCafe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.KentoCafe.dto.ingrediente.IngredienteRequest;
import school.sptech.KentoCafe.dto.ingrediente.IngredienteResponse;
import school.sptech.KentoCafe.entity.Ingrediente;
import school.sptech.KentoCafe.mapper.IngredienteMapper;
import school.sptech.KentoCafe.service.IngredienteService;

import java.util.List;

@RestController
@RequestMapping("/ingredientes")
public class IngredienteController {

    final IngredienteService ingredienteService;

    public IngredienteController(IngredienteService ingredienteService) {
        this.ingredienteService = ingredienteService;
    }

    @GetMapping
    public ResponseEntity<List<IngredienteResponse>> listarIngredientes(){
        List<Ingrediente> ingredientes = ingredienteService.buscarIngredientes();
        if (ingredientes.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(IngredienteMapper.toResponseList(ingredientes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredienteResponse> buscarIngredientePorId(
            @RequestParam Integer id
    ){
        Ingrediente ingrediente = ingredienteService.buscarIngredientePorId(id);
        if (ingrediente == null){
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).body(IngredienteMapper.toResponse(ingrediente));
    }

    @PostMapping
    public ResponseEntity<IngredienteResponse> criarIngrediente(
            @RequestBody IngredienteRequest req
    ){
        if (req.getNome() != null && !req.getNome().isBlank()){
            Ingrediente ingrediente = IngredienteMapper.toEntity(req);
            Ingrediente ingredienteCriado = ingredienteService.criarIngrediente(ingrediente);
            return ResponseEntity.status(201).body(IngredienteMapper.toResponse(ingredienteCriado));
        }
        return ResponseEntity.status(400).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredienteResponse> atualizarIngrediente(
            @RequestBody IngredienteRequest req,
            @RequestParam Integer id
    ){
        if (ingredienteService.buscarIngredientePorId(id) == null){
            return ResponseEntity.status(404).build();
        }
        Ingrediente ingrediente = ingredienteService.atualizarIngrediente(id, req);
        return ResponseEntity.status(200).body(IngredienteMapper.toResponse(ingrediente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(
            @RequestParam Integer id
    ){
        if (ingredienteService.buscarIngredientePorId(id) == null){
            return ResponseEntity.status(404).build();
        }
        ingredienteService.deletarPorId(id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/por-produto/{produtoId}")
    public ResponseEntity<List<IngredienteResponse>> buscarIngredientesPorProduto(
            @RequestParam Integer produtoId
    ){
        List<Ingrediente> ingredientes = ingredienteService.buscarIngredientePorProdutoId(produtoId);
        if (ingredientes.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(200).body(IngredienteMapper.toResponseList(ingredientes));
    }

}
