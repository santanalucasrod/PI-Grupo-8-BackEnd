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

    @Column(length = 30)
    private String tipo;

    @JsonIgnore
    @ManyToMany(mappedBy = "personalizacoes")
    private List<ItemPedido> itensPedido;

    public Personalizacao(Long id, String nome, String tipo, List<ItemPedido> itensPedido) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<ItemPedido> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(List<ItemPedido> itensPedido) {
        this.itensPedido = itensPedido;
    }
}