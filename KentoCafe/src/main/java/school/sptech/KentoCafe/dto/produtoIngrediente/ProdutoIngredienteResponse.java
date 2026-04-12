package school.sptech.KentoCafe.dto.produtoIngrediente;

public class ProdutoIngredienteResponse {
    private Integer id;
    private Produto produto;
    private Ingrediente ingrediente;

    public static class Produto {
        private Integer id;
        private String nome;
        private Integer categoriaId;
        private String categoriaNome;
        private Double precoUnidade;
        private String descricao;
        private String pathFt;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public Integer getCategoriaId() { return categoriaId; }
        public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }

        public String getCategoriaNome() { return categoriaNome; }
        public void setCategoriaNome(String categoriaNome) { this.categoriaNome = categoriaNome; }

        public Double getPrecoUnidade() { return precoUnidade; }
        public void setPrecoUnidade(Double precoUnidade) { this.precoUnidade = precoUnidade; }

        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }

        public String getPathFt() { return pathFt; }
        public void setPathFt(String pathFt) { this.pathFt = pathFt; }

        public Produto(Integer id, String nome, Integer categoriaId, String categoriaNome, Double precoUnidade, String descricao, String pathFt) {
            this.id = id;
            this.nome = nome;
            this.categoriaId = categoriaId;
            this.categoriaNome = categoriaNome;
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

    public ProdutoIngredienteResponse(Integer id, Produto produto, Ingrediente ingrediente) {
        this.id = id;
        this.produto = produto;
        this.ingrediente = ingrediente;
    }

    public ProdutoIngredienteResponse() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }
}
