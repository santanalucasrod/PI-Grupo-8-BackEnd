package school.sptech.KentoCafe.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.KentoCafe.dto.categoria.CategoriaRequest;
import school.sptech.KentoCafe.dto.categoria.CategoriaResponse;
import school.sptech.KentoCafe.entity.Categoria;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.mapper.CategoriaMapper;
import school.sptech.KentoCafe.repository.CategoriaRepository;
import school.sptech.KentoCafe.repository.ProdutoRepository;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository,
                            ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    public CategoriaResponse criar(CategoriaRequest dto) {
        Categoria categoria = CategoriaMapper.toEntity(dto);
        Categoria salva = categoriaRepository.save(categoria);
        return CategoriaMapper.toResponse(salva, List.of());
    }

    public List<CategoriaResponse> listarTodos() {
        return categoriaRepository.findAll()
                .stream()
                .map(categoria -> {
                    List<Produto> produtos = produtoRepository.findByCategoria_Id(categoria.getId());
                    return CategoriaMapper.toResponse(categoria, produtos);
                })
                .toList();
    }

    public CategoriaResponse buscarPorId(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoria não encontrada"));
        List<Produto> produtos = produtoRepository.findByCategoria_Id(id);
        return CategoriaMapper.toResponse(categoria, produtos);
    }

    public CategoriaResponse atualizar(Integer id, CategoriaRequest dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoria não encontrada"));

        categoria.setNome(dto.getNome());

        List<Produto> produtos = produtoRepository.findByCategoria_Id(id);
        return CategoriaMapper.toResponse(categoriaRepository.save(categoria), produtos);
    }

    public void deletar(Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada");
        }

        if (!produtoRepository.findByCategoria_Id(id).isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Categoria não pode ser deletada pois possui produtos vinculados");
        }

        categoriaRepository.deleteById(id);
    }
}