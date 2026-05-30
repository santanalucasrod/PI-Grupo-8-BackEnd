package school.sptech.KentoCafe.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.KentoCafe.dto.ingrediente.IngredienteRequest;
import school.sptech.KentoCafe.entity.Ingrediente;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.mapper.IngredienteMapper;
import school.sptech.KentoCafe.repository.IngredienteRepository;
import school.sptech.KentoCafe.repository.ProdutoRepository;

import java.util.List;

@Service
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;
    private final ProdutoRepository produtoRepository;

    public IngredienteService(IngredienteRepository ingredienteRepository, ProdutoRepository produtoRepository) {
        this.ingredienteRepository = ingredienteRepository;
        this.produtoRepository = produtoRepository;
    }

    public List<Ingrediente> buscarTodos() {
        return ingredienteRepository.findAll();
    }

    public Ingrediente buscarPorId(Long id) {
        return ingredienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Ingrediente não encontrado"));
    }

    public Ingrediente criar(Ingrediente ingrediente) {
        return ingredienteRepository.save(ingrediente);
    }

    public Ingrediente atualizar(Long id, IngredienteRequest req) {
        if (!ingredienteRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Ingrediente não encontrado");
        }
        Ingrediente ingredienteAtualizar = IngredienteMapper.toEntity(req);
        ingredienteAtualizar.setId(id);
        return ingredienteRepository.save(ingredienteAtualizar);
    }

    public void deletar(Long id) {
        if (!ingredienteRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Ingrediente não encontrado");
        }
        ingredienteRepository.removerIngredienteDeTodosProdutos(id);
        ingredienteRepository.deleteById(id);
    }

    public List<Ingrediente> buscarIngredientesPorProduto(Long produtoId) {
        return ingredienteRepository.findIngredientesByProdutoId(produtoId);
    }

    public Boolean existeProdutosComIngrediente(Long ingredienteId) {
        return produtoRepository.contarProdutosPorIngrediente(ingredienteId) != 0;
    }

    public List<Produto> buscarProdutosPorIngrediente(Long ingredienteId) {
        return produtoRepository.findByIngredienteId(ingredienteId);
    }
}
