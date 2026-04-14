package school.sptech.KentoCafe.mapper;

import school.sptech.KentoCafe.dto.produto.ProdutoRequest;
import school.sptech.KentoCafe.dto.produto.ProdutoResponse;
import school.sptech.KentoCafe.entity.Categoria;
import school.sptech.KentoCafe.entity.Produto;

import java.util.List;

public class ProdutoMapper {

    public static Produto toEntity(ProdutoRequest dto, Categoria categoria) {
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setCategoria(categoria);
        produto.setPrecoUnidade(dto.getPrecoUnidade());
        produto.setDescricao(dto.getDescricao());
        produto.setPathFt(dto.getPathFt());
        return produto;
    }

    public static ProdutoResponse toResponse(Produto produto) {
        ProdutoResponse dto = new ProdutoResponse();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        ProdutoResponse.Categoria categoria = new ProdutoResponse.Categoria(produto.getCategoria().getId(),produto.getCategoria().getNome());
        dto.setCategoria(categoria);
        dto.setPrecoUnidade(produto.getPrecoUnidade());
        dto.setDescricao(produto.getDescricao());
        dto.setPathFt(produto.getPathFt());
        return dto;
    }

    public static List<ProdutoResponse> toResponseList(List<Produto> produtos) {
        return produtos.stream()
                .map(ProdutoMapper::toResponse)
                .toList();
    }
}