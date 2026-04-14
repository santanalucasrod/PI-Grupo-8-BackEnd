package school.sptech.KentoCafe.dto.produto;

import school.sptech.KentoCafe.entity.Categoria;

public class ProdutoResponseDto {

    private Integer id;
    private String nome;
    private Categoria categoria;
    private Double precoUnidade;
    private String descricao;
    private String pathFt;

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

    public ProdutoResponseDto(Integer id, String nome, Categoria categoria, Double precoUnidade, String descricao, String pathFt) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.precoUnidade = precoUnidade;
        this.descricao = descricao;
        this.pathFt = pathFt;
    }

    public ProdutoResponseDto() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Double getPrecoUnidade() { return precoUnidade; }
    public void setPrecoUnidade(Double precoUnidade) { this.precoUnidade = precoUnidade; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getPathFt() { return pathFt; }
    public void setPathFt(String pathFt) { this.pathFt = pathFt; }
}