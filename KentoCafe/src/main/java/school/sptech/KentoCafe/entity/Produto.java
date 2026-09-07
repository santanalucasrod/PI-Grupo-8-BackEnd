package school.sptech.KentoCafe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "produto")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(name = "preco_unidade", precision = 5, scale = 2)
    private BigDecimal precoUnidade;

    @Column(length = 200)
    private String descricao;

    @Column(name = "path_ft", length = 200)
    private String pathFt;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private Boolean ativo = true;

    @ManyToMany
    @JoinTable(
            name = "produto_personalizacao",
            joinColumns = @JoinColumn(name = "produto_id"),
            inverseJoinColumns = @JoinColumn(name = "personalizacao_id")
    )
    private List<Personalizacao> personalizacoes;

    @ManyToMany
    @JoinTable(
            name = "produto_ingrediente",
            joinColumns = @JoinColumn(name = "produto_id"),
            inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
    )
    private List<Ingrediente> ingredientes;

    @JsonIgnore
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoTamanho> tamanhos;

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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPrecoUnidade() {
        return precoUnidade;
    }

    public void setPrecoUnidade(BigDecimal precoUnidade) {
        this.precoUnidade = precoUnidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPathFt() {
        return pathFt;
    }

    public void setPathFt(String pathFt) {
        this.pathFt = pathFt;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public List<Personalizacao> getPersonalizacoes() { return personalizacoes; }
    public void setPersonalizacoes(List<Personalizacao> personalizacoes) { this.personalizacoes = personalizacoes; }
    public List<ProdutoTamanho> getTamanhos() { return tamanhos; }
    public void setTamanhos(List<ProdutoTamanho> tamanhos) { this.tamanhos = tamanhos; }

    public Produto(Long id, String nome, Categoria categoria, BigDecimal precoUnidade, String descricao, String pathFt, Boolean ativo, List<Personalizacao> personalizacoes, List<Ingrediente> ingredientes, List<ProdutoTamanho> tamanhos) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.precoUnidade = precoUnidade;
        this.descricao = descricao;
        this.pathFt = pathFt;
        this.ativo = ativo;
        this.personalizacoes = personalizacoes;
        this.ingredientes = ingredientes;
        this.tamanhos = tamanhos;
    }

    public Produto(Long id, String nome, Categoria categoria, BigDecimal precoUnidade, String descricao, String pathFt, List<Ingrediente> ingredientes) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.precoUnidade = precoUnidade;
        this.descricao = descricao;
        this.pathFt = pathFt;
        this.ingredientes = ingredientes;
    }

    public Produto() {
    }
}