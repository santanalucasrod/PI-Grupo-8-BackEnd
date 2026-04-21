package school.sptech.KentoCafe.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.KentoCafe.dto.produto.ProdutoRequest;
import school.sptech.KentoCafe.dto.produto.ProdutoResponse;
import school.sptech.KentoCafe.dto.produtoIngrediente.ProdutoIngredienteResponse;
import school.sptech.KentoCafe.entity.Categoria;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.mapper.ProdutoMapper;
import school.sptech.KentoCafe.repository.CategoriaRepository;
import school.sptech.KentoCafe.repository.ProdutoIngredienteRepository;
import school.sptech.KentoCafe.repository.ProdutoRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    final ProdutoIngredienteRepository produtoIngredienteRepository;

    public ProdutoService(ProdutoRepository produtoRepository,
                          CategoriaRepository categoriaRepository, ProdutoIngredienteRepository produtoIngredienteRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.produtoIngredienteRepository = produtoIngredienteRepository;
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

    public ProdutoResponse buscarPorId(Integer id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produto não encontrado"));
        return ProdutoMapper.toResponse(produto);
    }

    public ProdutoResponse atualizar(Integer id, ProdutoRequest dto) {
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

    public void deletar(Integer id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }
        if (produtoIngredienteRepository.existsByProdutoId(id) != 0){
            produtoIngredienteRepository.deletarProdutoIngredientePorProdutoId(id);
            produtoRepository.deleteById(id);
        }
        produtoRepository.deleteById(id);
    }

    private Categoria buscarCategoriaPorId(Integer categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    public List<Produto> buscarProdutosPorIngredienteId(Integer id){
        return produtoRepository.findByIngredienteId(id);
    }

    public List<ProdutoResponse> listarPorCategoria(Integer id) {
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
}