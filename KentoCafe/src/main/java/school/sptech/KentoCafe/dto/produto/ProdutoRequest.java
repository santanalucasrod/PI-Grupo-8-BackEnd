package school.sptech.KentoCafe.dto.produto;

import jakarta.validation.constraints.*;

public class ProdutoRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 45, message = "Nome deve ter no máximo 45 caracteres")
    private String nome;

    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private Double precoUnidade;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
    private String descricao;

    @NotBlank(message = "Path da foto é obrigatório")
    @Size(max = 45, message = "Path da foto deve ter no máximo 45 caracteres")
    private String pathFt;

    private Categoria categoria;

    public static class Categoria{
        private Integer id;
        private String nome;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public Categoria(Integer id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        public Categoria() {
        }
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public ProdutoRequest(String nome, Double precoUnidade, String descricao, String pathFt, Categoria categoria) {
        this.nome = nome;
        this.precoUnidade = precoUnidade;
        this.descricao = descricao;
        this.pathFt = pathFt;
        this.categoria = categoria;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Double getPrecoUnidade() { return precoUnidade; }
    public void setPrecoUnidade(Double precoUnidade) { this.precoUnidade = precoUnidade; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getPathFt() { return pathFt; }
    public void setPathFt(String pathFt) { this.pathFt = pathFt; }
}