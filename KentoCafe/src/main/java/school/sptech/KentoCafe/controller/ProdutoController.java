package school.sptech.KentoCafe.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.KentoCafe.dto.produto.ProdutoRequestDto;
import school.sptech.KentoCafe.dto.produto.ProdutoResponseDto;
import school.sptech.KentoCafe.service.ProdutoService;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDto> criar(@RequestBody @Valid ProdutoRequestDto dto) {
        return ResponseEntity.status(201).body(produtoService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDto>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid ProdutoRequestDto dto) {
        return ResponseEntity.ok(produtoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}