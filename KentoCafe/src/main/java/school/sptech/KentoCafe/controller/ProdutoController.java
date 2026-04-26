package school.sptech.KentoCafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.hibernate.sql.ast.tree.expression.Summarization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.KentoCafe.dto.produto.ProdutoRequest;
import school.sptech.KentoCafe.dto.produto.ProdutoRequest;
import school.sptech.KentoCafe.dto.produto.ProdutoResponse;
import school.sptech.KentoCafe.dto.produto.ProdutoResponse;
import school.sptech.KentoCafe.entity.Ingrediente;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.mapper.IngredienteMapper;
import school.sptech.KentoCafe.mapper.ProdutoMapper;
import school.sptech.KentoCafe.service.ProdutoService;

import java.util.List;
import java.util.Map;
@Tag(name = "Produtos", description = "Gerenciamento do cardápio de produtos da cafeteria")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
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
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @Operation(summary = "Atualizar produto")
    @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso")
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid ProdutoRequest dto) {
        return ResponseEntity.ok(produtoService.atualizar(id, dto));
    }
    @Operation(summary = "Deletar produto")
    @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar produtos por ingrediente")
    @ApiResponse(responseCode = "200", description = "Produtos encontrados")
    @GetMapping("/por-ingrediente/{ingredienteId}")
    public ResponseEntity<List<ProdutoResponse>> buscarProdutosPorIngrediente(
            @RequestParam Integer ingredienteId
    ){
        List<Produto> produtos = produtoService.buscarProdutosPorIngredienteId(ingredienteId);
        if (produtos.isEmpty()){
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).body(ProdutoMapper.toResponseList(produtos));
    }

    @Operation(summary = "Listar produtos por categoria")
    @ApiResponse(responseCode = "200", description = "Produtos encontrados")
    @GetMapping("/categoria/{id}")
    public ResponseEntity<List<ProdutoResponse>> listarPorCategoria(@PathVariable Integer id) {
        List<ProdutoResponse> produtos = produtoService.listarPorCategoria(id);
        return produtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(produtos);
    }

    @Operation(summary = "Listar produtos agrupados por categoria", description = "Retorna um mapa com categorias como chave e lista de produtos como valor")
    @ApiResponse(responseCode = "200", description = "Produtos agrupados retornados com sucesso")
    @GetMapping("/agrupados")
    public ResponseEntity<Map<String, List<ProdutoResponse>>> listarAgrupados() {
        Map<String, List<ProdutoResponse>> agrupados = produtoService.listarPorCategoriaAgrupados();
        return ResponseEntity.ok(agrupados);
    }
}