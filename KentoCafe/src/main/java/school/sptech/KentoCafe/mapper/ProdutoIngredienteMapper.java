package school.sptech.KentoCafe.mapper;

import school.sptech.KentoCafe.dto.produtoIngrediente.ProdutoIngredienteRequest;
import school.sptech.KentoCafe.dto.produtoIngrediente.ProdutoIngredienteResponse;
import school.sptech.KentoCafe.entity.Categoria;
import school.sptech.KentoCafe.entity.Ingrediente;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.entity.ProdutoIngrediente;

import java.util.List;

public class ProdutoIngredienteMapper {

    public static ProdutoIngredienteResponse toResponse(ProdutoIngrediente produtoIngrediente) {
        ProdutoIngredienteResponse.Ingrediente ingrediente = new ProdutoIngredienteResponse.Ingrediente(produtoIngrediente.getIngrediente().getId(), produtoIngrediente.getIngrediente().getNome());
        ProdutoIngredienteResponse.Produto produto = new ProdutoIngredienteResponse.Produto(produtoIngrediente.getProduto().getId(),
                produtoIngrediente.getProduto().getNome(),
                produtoIngrediente.getProduto().getCategoria().getId(),
                produtoIngrediente.getProduto().getCategoria().getNome(),
                produtoIngrediente.getProduto().getPrecoUnidade(),
                produtoIngrediente.getProduto().getDescricao(),
                produtoIngrediente.getProduto().getPathFt());

        ProdutoIngredienteResponse res = new ProdutoIngredienteResponse();
        res.setIngrediente(ingrediente);
        res.setProduto(produto);
        return res;
    }

    public static ProdutoIngrediente toEntity(ProdutoIngredienteRequest req){
        ProdutoIngrediente produtoIngrediente = new ProdutoIngrediente();
        Ingrediente ingrediente = new Ingrediente(req.getIngrediente().getId(),req.getIngrediente().getNome());
        Categoria categoria = new Categoria(req.getProduto().getCategoria().getId(), req.getProduto().getCategoria().getNome());
        Produto produto = new Produto(req.getProduto().getId(),
                req.getProduto().getNome(),
                categoria,
                req.getProduto().getPrecoUnidade(),
                req.getProduto().getDescricao(),
                req.getProduto().getPathFt());

        produtoIngrediente.setIngrediente(ingrediente);
        produtoIngrediente.setProduto(produto);
        return produtoIngrediente;
    }

    public static List<ProdutoIngredienteResponse> toResponseList(List<ProdutoIngrediente> produtoIngredientes){
        return produtoIngredientes.stream()
                .map(ProdutoIngredienteMapper::toResponse)
                .toList();
    }
}
