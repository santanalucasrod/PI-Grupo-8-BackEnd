package school.sptech.KentoCafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @ApiResponse(responseCode = "204", description = "Nenhum ingrediente cadastrado")
    @GetMapping
    public ResponseEntity<List<IngredienteResponse>> listarTodos() {
        List<Ingrediente> ingredientes = ingredienteService.buscarTodos();
        if (ingredientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(IngredienteMapper.toResponseList(ingredientes));
    }

    @Operation(summary = "Buscar ingrediente por ID")
    @ApiResponse(responseCode = "200", description = "Ingrediente encontrado")
    @ApiResponse(responseCode = "404", description = "Ingrediente não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<IngredienteResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(IngredienteMapper.toResponse(ingredienteService.buscarPorId(id)));
    }

    @Operation(summary = "Criar ingrediente")
    @ApiResponse(responseCode = "201", description = "Ingrediente criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<IngredienteResponse> criar(
            @RequestBody @Valid IngredienteRequest req
    ) {
        Ingrediente ingrediente = IngredienteMapper.toEntity(req);
        Ingrediente criado = ingredienteService.criar(ingrediente);
        return ResponseEntity.status(201).body(IngredienteMapper.toResponse(criado));
    }

    @Operation(summary = "Atualizar ingrediente")
    @ApiResponse(responseCode = "200", description = "Ingrediente atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Ingrediente não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<IngredienteResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid IngredienteRequest req
    ) {
        return ResponseEntity.ok(IngredienteMapper.toResponse(ingredienteService.atualizar(id, req)));
    }

    @Operation(summary = "Deletar ingrediente")
    @ApiResponse(responseCode = "204", description = "Ingrediente deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Ingrediente não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id
    ) {
        ingredienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar ingredientes por produto")
    @ApiResponse(responseCode = "200", description = "Ingredientes encontrados")
    @ApiResponse(responseCode = "204", description = "Nenhum ingrediente encontrado")
    @GetMapping("/por-produto/{produtoId}")
    public ResponseEntity<List<IngredienteResponse>> buscarPorProduto(
            @PathVariable Long produtoId
    ) {
        List<Ingrediente> ingredientes = ingredienteService.buscarIngredientesPorProduto(produtoId);
        if (ingredientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(IngredienteMapper.toResponseList(ingredientes));
    }

}
