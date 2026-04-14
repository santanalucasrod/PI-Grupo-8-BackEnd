package school.sptech.KentoCafe.mapper;

import school.sptech.KentoCafe.dto.categoria.CategoriaRequest;
import school.sptech.KentoCafe.dto.categoria.CategoriaResponse;
import school.sptech.KentoCafe.entity.Categoria;
import school.sptech.KentoCafe.entity.Produto;

import java.util.List;

public class CategoriaMapper {

    public static Categoria toEntity(CategoriaRequest dto) {
        Categoria categoria = new Categoria();
        categoria.setNome(dto.getNome());
        return categoria;
    }

    public static CategoriaResponse toResponse(Categoria categoria, List<Produto> produtos) {
        CategoriaResponse dto = new CategoriaResponse();
        dto.setId(categoria.getId());
        dto.setNome(categoria.getNome());
        dto.setProdutos(
                produtos.stream()
                        .map(ProdutoMapper::toResponseDTO)
                        .toList()
        );
        return dto;
    }
}