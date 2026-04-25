package school.sptech.KentoCafe.controller;

import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Produtos", description = "Orquestrador de requisições envolvendo produto")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }
    @Operation(summary = "cria produto", description = "cria um novo produto ")
    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@RequestBody @Valid ProdutoRequest dto) {
        return ResponseEntity.status(201).body(produtoService.criar(dto));
    }
    @Operation(summary = "lista produtos", description = "lista todos os produtos existentes")
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @Operation(summary = "busca produtos pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @Operation(summary = "atualiza produto", description = "atualiza um produto a partir de seu id")
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid ProdutoRequest dto) {
        return ResponseEntity.ok(produtoService.atualizar(id, dto));
    }
@Operation(summary = "deleta produto", description = "deleta produto a partir do seu id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Busca produto por ingrediente", description = "a partir do id de um ingrediente é listados os produtos que o utilizam")
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

    @Operation(summary = "Busca produtos de uma categoria",description = "retorna uma lista de produtos que compoém uma categoria")
    @GetMapping("/categoria/{id}")
    public ResponseEntity<List<ProdutoResponse>> listarPorCategoria(@PathVariable Integer id) {
        List<ProdutoResponse> produtos = produtoService.listarPorCategoria(id);
        return produtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(produtos);
    }

    @Operation(summary = "Lista produtos agrupados", description = "lista os produtos agrupando por categorias")
    @GetMapping("/agrupados")
    public ResponseEntity<Map<String, List<ProdutoResponse>>> listarAgrupados() {
        Map<String, List<ProdutoResponse>> agrupados = produtoService.listarPorCategoriaAgrupados();
        return ResponseEntity.ok(agrupados);
    }
}