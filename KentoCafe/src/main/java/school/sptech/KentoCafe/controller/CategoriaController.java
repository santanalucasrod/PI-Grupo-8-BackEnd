package school.sptech.KentoCafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.KentoCafe.dto.categoria.CategoriaRequest;
import school.sptech.KentoCafe.dto.categoria.CategoriaResponse;
import school.sptech.KentoCafe.service.CategoriaService;

import java.util.List;

@Tag(name = "Categorias", description = "orquestrador das requisições envolvendo categorias de produtos")
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Operation(summary = "Criação da categoria", description = "cria uma nova categoria de produtos")
    @PostMapping
    public ResponseEntity<CategoriaResponse> criar(@RequestBody @Valid CategoriaRequest dto) {
        return ResponseEntity.status(201).body(categoriaService.criar(dto));
    }

    @Operation(summary = "Lista de categorias", description = "Retorna todas as categorias cadastradas")
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarTodos() {
        return ResponseEntity.ok(categoriaService.listarTodos());
    }

    @Operation(summary = "Buscar categoria por ID", description = "Busca uma categoria especifica pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }
    @Operation(summary = "Atualizar categoria", description = "atualiza informações de uma categoria")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid CategoriaRequest dto) {
        return ResponseEntity.ok(categoriaService.atualizar(id, dto));
    }
    @Operation(summary = "Deleta a categoria")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}