package school.sptech.KentoCafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.hibernate.sql.ast.tree.expression.Summarization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.KentoCafe.dto.ingrediente.IngredienteResponse;
import school.sptech.KentoCafe.dto.produto.ProdutoRequest;
import school.sptech.KentoCafe.dto.produto.ProdutoRequest;
import school.sptech.KentoCafe.dto.produto.ProdutoResponse;
import school.sptech.KentoCafe.dto.produto.ProdutoResponse;
import school.sptech.KentoCafe.entity.Ingrediente;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.mapper.IngredienteMapper;
import school.sptech.KentoCafe.mapper.ProdutoMapper;
import school.sptech.KentoCafe.service.IngredienteService;
import school.sptech.KentoCafe.service.ProdutoService;

import java.util.List;
import java.util.Map;
@Tag(name = "Produtos", description = "Gerenciamento do cardápio de produtos da cafeteria")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final IngredienteService ingredienteService;

    public ProdutoController(ProdutoService produtoService, IngredienteService ingredienteService) {
        this.produtoService = produtoService;
        this.ingredienteService = ingredienteService;
    }
    @Operation(summary = "Criar produto")
    @ApiResponse(responseCode = "201", description = "Produto criado com sucesso")
    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@RequestBody @Valid ProdutoRequest dto) {
        return ResponseEntity.status(201).body(produtoService.criar(dto));
    }
    @Operation(summary = "Listar produtos", description = "Retorna todos os produtos do cardápio")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @Operation(summary = "Buscar produto por ID")
    @ApiResponse(responseCode = "200", description = "Produto encontrado")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @Operation(summary = "Atualizar produto")
    @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso")
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProdutoRequest dto) {
        return ResponseEntity.ok(produtoService.atualizar(id, dto));
    }

    @Operation(summary = "Deletar produto")
    @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar produtos por categoria")
    @ApiResponse(responseCode = "200", description = "Produtos encontrados")
    @ApiResponse(responseCode = "204", description = "Nenhum produto encontrado")
    @GetMapping("/categoria/{id}")
    public ResponseEntity<List<ProdutoResponse>> listarPorCategoria(@PathVariable Long id) {
        List<ProdutoResponse> produtos = produtoService.listarPorCategoria(id);
        return produtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(produtos);
    }

    @Operation(summary = "Listar produtos agrupados por categoria")
    @ApiResponse(responseCode = "200", description = "Produtos agrupados retornados com sucesso")
    @GetMapping("/agrupados")
    public ResponseEntity<Map<String, List<ProdutoResponse>>> listarAgrupados() {
        return ResponseEntity.ok(produtoService.listarPorCategoriaAgrupados());
    }

    @Operation(summary = "Buscar ingredientes de um produto")
    @ApiResponse(responseCode = "200", description = "Ingredientes encontrados")
    @ApiResponse(responseCode = "204", description = "Nenhum ingrediente encontrado")
    @GetMapping("/{id}/ingredientes")
    public ResponseEntity<List<IngredienteResponse>> buscarIngredientes(
            @PathVariable Long id) {
        List<Ingrediente> ingredientes = produtoService.buscarIngredientesPorProduto(id);
        return ingredientes.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(IngredienteMapper.toResponseList(ingredientes));
    }

    @Operation(summary = "Adicionar ingrediente ao produto")
    @ApiResponse(responseCode = "200", description = "Ingrediente adicionado com sucesso")
    @PostMapping("/{id}/ingredientes/{ingredienteId}")
    public ResponseEntity<ProdutoResponse> adicionarIngrediente(
            @PathVariable Long id,
            @PathVariable Long ingredienteId) {
        Produto produto = produtoService.adicionarIngrediente(id, ingredienteId);
        return ResponseEntity.ok(ProdutoMapper.toResponse(produto));
    }

    @Operation(summary = "Remover ingrediente do produto")
    @ApiResponse(responseCode = "204", description = "Ingrediente removido com sucesso")
    @DeleteMapping("/{id}/ingredientes/{ingredienteId}")
    public ResponseEntity<Void> removerIngrediente(
            @PathVariable Long id,
            @PathVariable Long ingredienteId) {
        produtoService.removerIngredienteDoProduto(id, ingredienteId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar produtos por ingrediente")
    @ApiResponse(responseCode = "200", description = "Produtos encontrados")
    @ApiResponse(responseCode = "204", description = "Nenhum produto encontrado")
    @GetMapping("/por-ingrediente/{ingredienteId}")
    public ResponseEntity<List<ProdutoResponse>> buscarPorIngrediente(
            @PathVariable Long ingredienteId) {
        List<Produto> produtos = ingredienteService.buscarProdutosPorIngrediente(ingredienteId);
        return produtos.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(ProdutoMapper.toResponseList(produtos));
    }

    @Operation(summary = "Atualizar todos os ingredientes de um produto",
            description = "Substitui toda a lista de ingredientes do produto")
    @ApiResponse(responseCode = "200", description = "Ingredientes atualizados")
    @ApiResponse(responseCode = "404", description = "Produto ou ingrediente não encontrado")
    @PutMapping("/{id}/ingredientes")
    public ResponseEntity<ProdutoResponse> atualizarIngredientes(
            @PathVariable Long id,
            @RequestBody List<Long> ingredienteIds) {
        Produto produto = produtoService.atualizarIngredientes(id, ingredienteIds);
        return ResponseEntity.ok(ProdutoMapper.toResponse(produto));
    }
}