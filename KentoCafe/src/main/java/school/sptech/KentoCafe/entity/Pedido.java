package school.sptech.KentoCafe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "dt_hr_pedido")
    private LocalDateTime dtHrPedido;
    @Column(name = "dt_hr_pronto")
    private LocalDateTime dtHrPronto;
    private String infoAdicional;
    private String status;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<Venda> itens;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDtHrPedido() {
        return dtHrPedido;
    }

    public void setDtHrPedido(LocalDateTime dtHrPedido) {
        this.dtHrPedido = dtHrPedido;
    }

    public LocalDateTime getDtHrPronto() {
        return dtHrPronto;
    }

    public void setDtHrPronto(LocalDateTime dtHrPronto) {
        this.dtHrPronto = dtHrPronto;
    }

    public String getInfoAdicional() {
        return infoAdicional;
    }

    public void setInfoAdicional(String infoAdicional) {
        this.infoAdicional = infoAdicional;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Venda> getItens() {
        return itens;
    }

    public void setItens(List<Venda> itens) {
        this.itens = itens;
    }
}
