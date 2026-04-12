package school.sptech.KentoCafe.service;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import school.sptech.KentoCafe.dto.produtoIngrediente.ProdutoIngredienteRequest;
import school.sptech.KentoCafe.dto.produtoIngrediente.ProdutoIngredienteResponse;
import school.sptech.KentoCafe.entity.Categoria;
import school.sptech.KentoCafe.entity.Ingrediente;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.entity.ProdutoIngrediente;
import school.sptech.KentoCafe.repository.IngredienteRepository;
import school.sptech.KentoCafe.repository.ProdutoIngredienteRepository;
import school.sptech.KentoCafe.repository.ProdutoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoIngredienteService {

    final ProdutoIngredienteRepository produtoIngredienteRepository;
    final ProdutoRepository produtoRepository;
    final IngredienteRepository ingredienteRepository;

    public ProdutoIngredienteService(ProdutoIngredienteRepository produtoIngredienteRepository, ProdutoRepository produtoRepository, IngredienteRepository ingredienteRepository) {
        this.produtoIngredienteRepository = produtoIngredienteRepository;
        this.produtoRepository = produtoRepository;
        this.ingredienteRepository = ingredienteRepository;
    }

    public ProdutoIngrediente buscarProdutoIngredientePorId(Integer id){
        if (produtoIngredienteRepository.existsById(id)){
            Optional<ProdutoIngrediente> produtoIngredienteOpt = produtoIngredienteRepository.findById(id);
            return produtoIngredienteOpt.get();
        }
        return null;
    }

    public void excluirPorId(Integer id) {
        produtoIngredienteRepository.deleteById(id);
    }

    public Boolean existeProdutoIngredientePorIngredienteId(Integer id){
        if (produtoIngredienteRepository.existsByIngredienteId(id) != 0){
            return true;
        }
        return false;
    }

    public Boolean existeProdutoIngredientePorProdutoId(Integer id){
        if (produtoIngredienteRepository.existsByProdutoId(id) != 0){
            return true;
        }
        return false;
    }

    @Modifying
    public void deleteProdutoIngredientePorProdutoId(Integer id){
        produtoIngredienteRepository.deletarProdutoIngredientePorProdutoId(id);
    }

    @Modifying
    public void deleteProdutoIngredientePorIngredienteId(Integer id){
        produtoIngredienteRepository.deletarProdutoIngredientePorIngredienteId(id);
    }

    public List<ProdutoIngrediente> buscarProdutoIngredientePorIngredienteId(Integer id){
        return produtoIngredienteRepository.findByIngredienteId(id);
    }

    public List<ProdutoIngrediente> buscarProdutoIngredientePorProdutoId(Integer id){
        return produtoIngredienteRepository.findByProdutoId(id);
    }

    public List<ProdutoIngrediente> buscarProdutoIngredientes(){
        return  produtoIngredienteRepository.findAll();
    }

    public ProdutoIngrediente criarProdutoIngrediente(ProdutoIngredienteRequest req) {
        ProdutoIngredienteRequest.Produto produto = req.getProduto();
        ProdutoIngredienteRequest.Ingrediente ingrediente = req.getIngrediente();
        Categoria categoria = new Categoria(produto.getCategoria().getId(), produto.getCategoria().getNome());

        Produto produtoEntidade = new Produto(produto.getId(), produto.getNome(), categoria, produto.getPrecoUnidade(), produto.getDescricao(), produto.getPathFt());
        Ingrediente IngredienteEntidade = new Ingrediente(ingrediente.getId(), ingrediente.getNome());
        ProdutoIngrediente produtoIngrediente = new ProdutoIngrediente();
        produtoIngrediente.setProduto(produtoEntidade);
        produtoIngrediente.setIngrediente(IngredienteEntidade);
        return produtoIngrediente;
    }

    public ProdutoIngrediente editarProdutoIngrediente(Integer id, ProdutoIngredienteRequest req){
        ProdutoIngredienteRequest.Produto produto = req.getProduto();
        ProdutoIngredienteRequest.Ingrediente ingrediente = req.getIngrediente();
        Categoria categoria = new Categoria(produto.getCategoria().getId(), produto.getCategoria().getNome());
        Produto produtoEntidade = new Produto(produto.getId(), produto.getNome(), categoria, produto.getPrecoUnidade(), produto.getDescricao(), produto.getPathFt());
        Ingrediente IngredienteEntidade = new Ingrediente(ingrediente.getId(), ingrediente.getNome());

        ProdutoIngrediente produtoIngrediente = new ProdutoIngrediente();
        produtoIngrediente.setId(id);
        produtoIngrediente.setIngrediente(IngredienteEntidade);
        produtoIngrediente.setProduto(produtoEntidade);

        return produtoIngrediente;
    }

}
