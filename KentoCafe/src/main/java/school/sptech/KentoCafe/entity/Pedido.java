package school.sptech.KentoCafe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "dt_hr_pedido")
    private LocalDate dtHrPedido;
    @Column(name = "dt_hr_pronto")
    private LocalDate dtHrPronto;
    private String infoAdicional;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDtHrPedido() {
        return dtHrPedido;
    }

    public void setDtHrPedido(LocalDate dtHrPedido) {
        this.dtHrPedido = dtHrPedido;
    }

    public LocalDate getDtHrPronto() {
        return dtHrPronto;
    }

    public void setDtHrPronto(LocalDate dtHrPronto) {
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
}
