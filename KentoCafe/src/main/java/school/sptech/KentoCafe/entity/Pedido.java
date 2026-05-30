package school.sptech.KentoCafe.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_cliente", nullable = false, length = 45)
    private String nomeCliente;

    @Column(name = "dt_hr_pedido", nullable = false)
    private LocalDateTime dtHrPedido;

    @Column(name = "dt_hr_pronto")
    private LocalDateTime dtHrPronto;

    @Column(name = "valor_total", precision = 6, scale = 2)
    private BigDecimal valorTotal;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens;

    public Pedido(Long id, String nomeCliente, LocalDateTime dtHrPedido, LocalDateTime dtHrPronto, BigDecimal valorTotal, Status status, Funcionario funcionario, List<ItemPedido> itens) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.dtHrPedido = dtHrPedido;
        this.dtHrPronto = dtHrPronto;
        this.valorTotal = valorTotal;
        this.status = status;
        this.funcionario = funcionario;
        this.itens = itens;
    }

    public Pedido() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
}
