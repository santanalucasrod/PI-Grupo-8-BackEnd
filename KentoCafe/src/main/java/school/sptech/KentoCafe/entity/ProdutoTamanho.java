package school.sptech.KentoCafe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produto_tamanho")
public class ProdutoTamanho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "tamanho_id", nullable = false)
    private Tamanho tamanho;

    @Column(name = "preco_unidade", nullable = false, precision = 5, scale = 2)
    private BigDecimal precoUnidade;

    public ProdutoTamanho() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public Tamanho getTamanho() { return tamanho; }
    public void setTamanho(Tamanho tamanho) { this.tamanho = tamanho; }
    public BigDecimal getPrecoUnidade() { return precoUnidade; }
    public void setPrecoUnidade(BigDecimal precoUnidade) { this.precoUnidade = precoUnidade; }
}