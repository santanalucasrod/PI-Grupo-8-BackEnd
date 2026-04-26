package school.sptech.KentoCafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import school.sptech.KentoCafe.dto.ingrediente.IngredienteRequest;
import school.sptech.KentoCafe.dto.ingrediente.IngredienteResponse;
import school.sptech.KentoCafe.entity.Ingrediente;
import school.sptech.KentoCafe.mapper.IngredienteMapper;
import school.sptech.KentoCafe.service.IngredienteService;

import javax.swing.plaf.SeparatorUI;
import java.util.List;
@Tag(name = "Ingredientes", description = "Gerenciamento de ingredientes dos produtos")
@RestController
@RequestMapping("/ingredientes")
public class IngredienteController {

    final IngredienteService ingredienteService;

    public IngredienteController(IngredienteService ingredienteService) {
        this.ingredienteService = ingredienteService;
    }

    @Operation(summary = "Listar ingredientes")
    @GetMapping
    public ResponseEntity<List<IngredienteResponse>> listarIngredientes(){
        List<Ingrediente> ingredientes = ingredienteService.buscarIngredientes();
        if (ingredientes.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(IngredienteMapper.toResponseList(ingredientes));
    }

    @Operation(summary = "Buscar ingrediente por ID")
    @ApiResponse(responseCode = "200", description = "Ingrediente encontrado")
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

    @Operation(summary = "Criar ingrediente")
    @ApiResponse(responseCode = "201", description = "Ingrediente criado com sucesso")
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

    @Operation(summary = "Atualizar ingrediente")
    @ApiResponse(responseCode = "200", description = "Ingrediente atualizado com sucesso")
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

    @Operation(summary = "Deletar ingrediente")
    @ApiResponse(responseCode = "204", description = "Ingrediente deletado com sucesso")
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

    @Operation(summary = "Buscar ingredientes por produto", description = "Lista todos os ingredientes de um produto específico")
    @ApiResponse(responseCode = "200", description = "Ingredientes encontrados")
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
