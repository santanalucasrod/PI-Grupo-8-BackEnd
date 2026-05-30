package school.sptech.KentoCafe.dto.produto;

import java.math.BigDecimal;

public class ProdutoResponse {

    private Long id;
    private String nome;
    private Categoria categoria;
    private BigDecimal precoUnidade;
    private String descricao;
    private String pathFt;

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

    public ProdutoResponse(Long id, String nome, Categoria categoria, BigDecimal precoUnidade, String descricao, String pathFt) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.precoUnidade = precoUnidade;
        this.descricao = descricao;
        this.pathFt = pathFt;
    }

    public ProdutoResponse() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getPrecoUnidade() { return precoUnidade; }
    public void setPrecoUnidade(BigDecimal precoUnidade) { this.precoUnidade = precoUnidade; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getPathFt() { return pathFt; }
    public void setPathFt(String pathFt) { this.pathFt = pathFt; }
}