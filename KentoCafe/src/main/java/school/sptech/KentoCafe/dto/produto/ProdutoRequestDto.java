package school.sptech.KentoCafe.dto.produto;

import jakarta.validation.constraints.*;

public class ProdutoRequestDto {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 45, message = "Nome deve ter no máximo 45 caracteres")
    private String nome;

    @NotNull(message = "Categoria é obrigatória")
    private Integer categoriaId;

    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private Double precoUnidade;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
    private String descricao;

    @NotBlank(message = "Path da foto é obrigatório")
    @Size(max = 45, message = "Path da foto deve ter no máximo 45 caracteres")
    private String pathFt;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }

    public Double getPrecoUnidade() { return precoUnidade; }
    public void setPrecoUnidade(Double precoUnidade) { this.precoUnidade = precoUnidade; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getPathFt() { return pathFt; }
    public void setPathFt(String pathFt) { this.pathFt = pathFt; }
}