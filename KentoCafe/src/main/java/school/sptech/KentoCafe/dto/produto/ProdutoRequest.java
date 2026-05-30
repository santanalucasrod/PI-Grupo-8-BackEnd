package school.sptech.KentoCafe.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Dados para criação ou atualização de produto")
public class ProdutoRequest {

    @Schema(description = "Nome do produto", example = "Cappuccino")
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 45)
    private String nome;

    @Schema(description = "Preço unitário do produto", example = "12.50")
    @DecimalMin(value = "0.01")
    private BigDecimal precoUnidade;

    @Schema(description = "Descrição do produto", example = "Café espresso com leite vaporizado")
    @NotBlank
    @Size(max = 200)
    private String descricao;

    @Schema(description = "Caminho da foto do produto", example = "cappuccino.jpg")
    @NotBlank
    @Size(max = 45)
    private String pathFt;

    private Categoria categoria;

    public static class Categoria{
        private Long id;
        private String nome;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public Categoria(Long id, String nome) {
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

    public ProdutoRequest(String nome, BigDecimal precoUnidade, String descricao, String pathFt, Categoria categoria) {
        this.nome = nome;
        this.precoUnidade = precoUnidade;
        this.descricao = descricao;
        this.pathFt = pathFt;
        this.categoria = categoria;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getPrecoUnidade() {
        return precoUnidade;
    }
    public void setPrecoUnidade(BigDecimal precoUnidade) {
        this.precoUnidade = precoUnidade;
    }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getPathFt() { return pathFt; }
    public void setPathFt(String pathFt) { this.pathFt = pathFt; }
}