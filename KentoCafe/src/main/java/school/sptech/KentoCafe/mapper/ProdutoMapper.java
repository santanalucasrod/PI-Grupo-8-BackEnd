package school.sptech.KentoCafe.mapper;

import school.sptech.KentoCafe.dto.produto.ProdutoRequestDto;
import school.sptech.KentoCafe.dto.produto.ProdutoResponseDto;
import school.sptech.KentoCafe.entity.Categoria;
import school.sptech.KentoCafe.entity.Produto;

public class ProdutoMapper {

    public static Produto toEntity(ProdutoRequestDto dto, Categoria categoria) {
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setCategoria(categoria);
        produto.setPrecoUnidade(dto.getPrecoUnidade());
        produto.setDescricao(dto.getDescricao());
        produto.setPathFt(dto.getPathFt());
        return produto;
    }

    public static ProdutoResponseDto toResponseDTO(Produto produto) {
        ProdutoResponseDto dto = new ProdutoResponseDto();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        ProdutoResponseDto.Categoria categoria = new ProdutoResponseDto.Categoria(produto.getCategoria().getId(),produto.getCategoria().getNome());
        dto.setCategoria(categoria);
        dto.setPrecoUnidade(produto.getPrecoUnidade());
        dto.setDescricao(produto.getDescricao());
        dto.setPathFt(produto.getPathFt());
        return dto;
    }
}