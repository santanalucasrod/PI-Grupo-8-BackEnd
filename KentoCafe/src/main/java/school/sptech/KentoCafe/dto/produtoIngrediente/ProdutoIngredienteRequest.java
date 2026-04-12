package school.sptech.KentoCafe.dto.produtoIngrediente;

import school.sptech.KentoCafe.entity.Categoria;

public class ProdutoIngredienteRequest {
    private ProdutoIngredienteResponse.Produto produto;
    private ProdutoIngredienteResponse.Ingrediente ingrediente;

    public static class Produto {
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

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public Categoria getCategoria() {
            return categoria;
        }

        public void setCategoria(Categoria categoria) {
            this.categoria = categoria;
        }

        public Double getPrecoUnidade() { return precoUnidade; }
        public void setPrecoUnidade(Double precoUnidade) { this.precoUnidade = precoUnidade; }

        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }

        public String getPathFt() { return pathFt; }
        public void setPathFt(String pathFt) { this.pathFt = pathFt; }

        public Produto(Integer id, String nome, Categoria categoria, Double precoUnidade, String descricao, String pathFt) {
            this.id = id;
            this.nome = nome;
            this.categoria = categoria;
            this.precoUnidade = precoUnidade;
            this.descricao = descricao;
            this.pathFt = pathFt;
        }

        public Produto() {
        }
    }

    public static class Ingrediente {
        private Integer id;
        private String nome;

        public Ingrediente(Integer id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        public Ingrediente() {
        }

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
    }

    public ProdutoIngredienteRequest(ProdutoIngredienteResponse.Produto produto, ProdutoIngredienteResponse.Ingrediente ingrediente) {
        this.produto = produto;
        this.ingrediente = ingrediente;
    }

    public ProdutoIngredienteRequest() {
    }

    public ProdutoIngredienteResponse.Produto getProduto() {
        return produto;
    }

    public void setProduto(ProdutoIngredienteResponse.Produto produto) {
        this.produto = produto;
    }

    public ProdutoIngredienteResponse.Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(ProdutoIngredienteResponse.Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }
}
