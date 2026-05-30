package school.sptech.KentoCafe.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.KentoCafe.dto.produto.ProdutoRequest;
import school.sptech.KentoCafe.dto.produto.ProdutoResponse;
import school.sptech.KentoCafe.entity.Categoria;
import school.sptech.KentoCafe.entity.Ingrediente;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.mapper.ProdutoMapper;
import school.sptech.KentoCafe.repository.CategoriaRepository;
import school.sptech.KentoCafe.repository.IngredienteRepository;
import school.sptech.KentoCafe.repository.ItemPedidoRepository;
import school.sptech.KentoCafe.repository.ProdutoRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final IngredienteRepository ingredienteRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public ProdutoService(ProdutoRepository produtoRepository,
                          CategoriaRepository categoriaRepository, IngredienteRepository ingredienteRepository, ItemPedidoRepository itemPedidoRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public ProdutoResponse criar(ProdutoRequest dto) {
        Categoria categoria = buscarCategoriaPorId(dto.getCategoria().getId());
        Produto produto = ProdutoMapper.toEntity(dto, categoria);
        return ProdutoMapper.toResponse(produtoRepository.save(produto));
    }

    public List<ProdutoResponse> listarTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(ProdutoMapper::toResponse)
                .toList();
    }

    public ProdutoResponse buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produto não encontrado"));
        return ProdutoMapper.toResponse(produto);
    }

    public ProdutoResponse atualizar(Long id, ProdutoRequest dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produto não encontrado"));

        Categoria categoria = buscarCategoriaPorId(dto.getCategoria().getId());

        produto.setNome(dto.getNome());
        produto.setCategoria(categoria);
        produto.setPrecoUnidade(dto.getPrecoUnidade());
        produto.setDescricao(dto.getDescricao());
        produto.setPathFt(dto.getPathFt());

        return ProdutoMapper.toResponse(produtoRepository.save(produto));
    }

    public void deletar(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Produto não encontrado");
        }

        // ✅ verifica se o produto está em algum pedido
        if (itemPedidoRepository.existsByProdutoId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Produto não pode ser deletado pois está vinculado a pedidos existentes");
        }

        // remove vínculos com ingredientes e deleta
        produtoRepository.removerTodosIngredientesDoProduto(id);
        produtoRepository.deleteById(id);
    }

    private Categoria buscarCategoriaPorId(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    public List<ProdutoResponse> listarPorCategoria(Long id) {
        return ProdutoMapper.toResponseList(produtoRepository.findByCategoriaId(id));
    }

    public Map<String, List<ProdutoResponse>> listarPorCategoriaAgrupados() {
        List<Produto> todos = produtoRepository.findAll();

        Map<String, List<Produto>> produtosAgrupados = todos.stream()
                .collect(Collectors.groupingBy(p -> p.getCategoria().getNome()));

        return produtosAgrupados.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> ProdutoMapper.toResponseList(e.getValue())
                ));
    }

    public void removerIngredienteDoProduto(Long produtoId, Long ingredienteId) {
        produtoRepository.removerIngredienteDoProduto(produtoId, ingredienteId);
    }

    public List<Ingrediente> buscarIngredientesPorProduto(Long produtoId) {
        return ingredienteRepository.findIngredientesByProdutoId(produtoId);
    }


    public Produto adicionarIngrediente(Long produtoId, Long ingredienteId) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produto não encontrado"));

        Ingrediente ingrediente = ingredienteRepository.findById(ingredienteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Ingrediente não encontrado"));

        if (produto.getIngredientes().contains(ingrediente)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ingrediente já vinculado a esse produto");
        }

        produto.getIngredientes().add(ingrediente);
        return produtoRepository.save(produto);
    }

    public Produto atualizarIngredientes(Long produtoId, List<Long> ingredienteIds) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produto não encontrado"));

        List<Ingrediente> novosIngredientes = ingredienteRepository.findAllById(ingredienteIds);

        if (novosIngredientes.size() != ingredienteIds.size()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Um ou mais ingredientes não encontrados");
        }

        produto.getIngredientes().clear();
        produto.getIngredientes().addAll(novosIngredientes);
        return produtoRepository.save(produto);
    }

}