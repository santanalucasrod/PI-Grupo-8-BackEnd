package school.sptech.KentoCafe.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.KentoCafe.dto.produto.ProdutoRequest;
import school.sptech.KentoCafe.dto.produto.ProdutoResponse;
import school.sptech.KentoCafe.dto.tamanho.produtotamanho.ProdutoTamanhoRequest;
import school.sptech.KentoCafe.dto.tamanho.produtotamanho.ProdutoTamanhoResponse;
import school.sptech.KentoCafe.entity.*;
import school.sptech.KentoCafe.mapper.ProdutoMapper;
import school.sptech.KentoCafe.repository.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final IngredienteRepository ingredienteRepository;
    private final TamanhoRepository tamanhoRepository;
    private final PersonalizacaoRepository personalizacaoRepository;
    private final ProdutoTamanhoRepository produtoTamanhoRepository;

    public ProdutoService(ProdutoRepository produtoRepository,
                          CategoriaRepository categoriaRepository, IngredienteRepository ingredienteRepository, TamanhoRepository tamanhoRepository, PersonalizacaoRepository personalizacaoRepository, ProdutoTamanhoRepository produtoTamanhoRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.ingredienteRepository = ingredienteRepository;

        this.tamanhoRepository = tamanhoRepository;
        this.personalizacaoRepository = personalizacaoRepository;
        this.produtoTamanhoRepository = produtoTamanhoRepository;
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

    public List<ProdutoResponse> listarTodosAtivos() {
        return produtoRepository.findByAtivoTrue()
                .stream().map(ProdutoMapper::toResponse).toList();
    }

    public ProdutoResponse buscarPorId(Long id) {
        return ProdutoMapper.toResponse(buscarEntidadePorId(id));
    }

    public ProdutoResponse atualizar(Long id, ProdutoRequest dto) {
        Produto produto = buscarEntidadePorId(id);
        Categoria categoria = buscarCategoriaPorId(dto.getCategoria().getId());

        produto.setNome(dto.getNome());
        produto.setCategoria(categoria);
        produto.setPrecoUnidade(dto.getPrecoUnidade());
        produto.setDescricao(dto.getDescricao());
        produto.setPathFt(dto.getPathFt());

        return ProdutoMapper.toResponse(produtoRepository.save(produto));
    }

    @Transactional
    public void deletar(Long id) {
        Produto produto = buscarEntidadePorId(id);
        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    public ProdutoResponse reativar(Long id) {
        Produto produto = buscarEntidadePorId(id);
        produto.setAtivo(true);
        return ProdutoMapper.toResponse(produtoRepository.save(produto));
    }

    private Categoria buscarCategoriaPorId(Long categoriaId) {
        return buscarCategoriaId(categoriaId);
    }

    public List<ProdutoResponse> listarPorCategoria(Long id) {
        return ProdutoMapper.toResponseList(produtoRepository.findByCategoriaId(id));
    }

    public Map<String, List<ProdutoResponse>> listarPorCategoriaAgrupados() {
        List<Produto> ativos = produtoRepository.findByAtivoTrue();
        return ativos.stream()
                .collect(Collectors.groupingBy(p -> p.getCategoria().getNome()))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> ProdutoMapper.toResponseList(e.getValue())));
    }

    @Transactional
    public void removerIngredienteDoProduto(Long produtoId, Long ingredienteId) {
        produtoRepository.removerIngredienteDoProduto(produtoId, ingredienteId);
    }

    public List<Ingrediente> buscarIngredientesPorProduto(Long produtoId) {
        return ingredienteRepository.findIngredientesByProdutoId(produtoId);
    }


    @Transactional
    public Produto adicionarIngrediente(Long produtoId, Long ingredienteId) {
        Produto produto = buscarEntidadePorId(produtoId);
        Ingrediente ingrediente = ingredienteRepository.findById(ingredienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingrediente não encontrado"));
        if (produto.getIngredientes().contains(ingrediente)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ingrediente já vinculado a esse produto");
        }
        produto.getIngredientes().add(ingrediente);
        return produtoRepository.save(produto);
    }

    @Transactional
    public Produto atualizarIngredientes(Long produtoId, List<Long> ingredienteIds) {
        Produto produto = buscarEntidadePorId(produtoId);
        List<Ingrediente> novos = ingredienteRepository.findAllById(ingredienteIds);
        if (novos.size() != ingredienteIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Um ou mais ingredientes não encontrados");
        }
        produto.getIngredientes().clear();
        produto.getIngredientes().addAll(novos);
        return produtoRepository.save(produto);
    }

    public List<Personalizacao> buscarPersonalizacoesPorProduto(Long produtoId) {
        return buscarEntidadePorId(produtoId).getPersonalizacoes();
    }

    @Transactional
    public Produto adicionarPersonalizacao(Long produtoId, Long personalizacaoId) {
        Produto produto = buscarEntidadePorId(produtoId);
        Personalizacao personalizacao = personalizacaoRepository.findById(personalizacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personalização não encontrada"));
        if (produto.getPersonalizacoes().contains(personalizacao)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Personalização já vinculada a esse produto");
        }
        produto.getPersonalizacoes().add(personalizacao);
        return produtoRepository.save(produto);
    }

    @Transactional
    public void removerPersonalizacao(Long produtoId, Long personalizacaoId) {
        Produto produto = buscarEntidadePorId(produtoId);
        produto.getPersonalizacoes().removeIf(p -> p.getId().equals(personalizacaoId));
        produtoRepository.save(produto);
    }

    @Transactional
    public Produto atualizarPersonalizacoes(Long produtoId, List<Long> personalizacaoIds) {
        Produto produto = buscarEntidadePorId(produtoId);
        List<Personalizacao> novas = personalizacaoRepository.findAllById(personalizacaoIds);
        if (novas.size() != personalizacaoIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Uma ou mais personalizações não encontradas");
        }
        produto.getPersonalizacoes().clear();
        produto.getPersonalizacoes().addAll(novas);
        return produtoRepository.save(produto);
    }

    public List<ProdutoTamanhoResponse> buscarTamanhosPorProduto(Long produtoId) {
        buscarEntidadePorId(produtoId);
        return produtoTamanhoRepository.findByProdutoId(produtoId).stream()
                .map(pt -> new ProdutoTamanhoResponse(
                        pt.getTamanho().getId(), pt.getTamanho().getNome(),
                        pt.getTamanho().getVolumeMl(), pt.getPrecoUnidade()))
                .toList();
    }

    @Transactional
    public ProdutoTamanhoResponse adicionarTamanho(Long produtoId, ProdutoTamanhoRequest dto) {
        Produto produto = buscarEntidadePorId(produtoId);
        Tamanho tamanho = tamanhoRepository.findById(dto.getTamanhoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tamanho não encontrado"));

        if (produtoTamanhoRepository.findByProdutoIdAndTamanhoId(produtoId, dto.getTamanhoId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esse tamanho já está cadastrado para esse produto");
        }

        ProdutoTamanho pt = new ProdutoTamanho();
        pt.setProduto(produto);
        pt.setTamanho(tamanho);
        pt.setPrecoUnidade(dto.getPrecoUnidade());
        produtoTamanhoRepository.save(pt);

        return new ProdutoTamanhoResponse(tamanho.getId(), tamanho.getNome(), tamanho.getVolumeMl(), dto.getPrecoUnidade());
    }

    @Transactional
    public void removerTamanho(Long produtoId, Long tamanhoId) {
        ProdutoTamanho pt = produtoTamanhoRepository.findByProdutoIdAndTamanhoId(produtoId, tamanhoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Esse produto não tem esse tamanho cadastrado"));
        produtoTamanhoRepository.delete(pt);
    }

    private Produto buscarEntidadePorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    private Categoria buscarCategoriaId(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }
}