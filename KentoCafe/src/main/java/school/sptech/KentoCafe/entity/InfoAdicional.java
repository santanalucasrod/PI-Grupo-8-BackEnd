package school.sptech.KentoCafe.entity;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "info_adicional")
public class InfoAdicional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String descricao;
    private String preferenciaIndividual;
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    public InfoAdicional(){

    }

    public InfoAdicional(Integer id, String descricao, String preferenciaIndividual, Pedido pedido) {
        this.id = id;
        this.descricao = descricao;
        this.preferenciaIndividual = preferenciaIndividual;
        this.pedido = pedido;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPreferenciaIndividual() {
        return preferenciaIndividual;
    }

    public void setPreferenciaIndividual(String preferenciaIndividual) {
        this.preferenciaIndividual = preferenciaIndividual;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}
