package school.sptech.KentoCafe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "personalizacao")
public class Personalizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60, unique = true)
    private String nome;

    @JsonIgnore
    @ManyToMany(mappedBy = "personalizacoes")
    private List<ItemPedido> itensPedido;

    @JsonIgnore
    @ManyToMany(mappedBy = "personalizacoes")
    private List<Produto> produtos;

    public Personalizacao(Long id, String nome, List<ItemPedido> itensPedido, List<Produto> produtos) {
        this.id = id;
        this.nome = nome;
        this.itensPedido = itensPedido;
        this.produtos = produtos;
    }

    public Personalizacao(Long id, String nome, List<ItemPedido> itensPedido) {
        this.id = id;
        this.nome = nome;
        this.itensPedido = itensPedido;
    }

    public Personalizacao() {
    }

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

    public List<ItemPedido> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(List<ItemPedido> itensPedido) {
        this.itensPedido = itensPedido;
    }

    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }
}