package school.sptech.KentoCafe.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private String status;
    private String nome;
    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<Venda> itens = new ArrayList<>();
    @ManyToOne
    private InfoAdicional infoAdicional;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Venda> getItens() {
        return itens;
    }

    public void setItens(List<Venda> itens) {
        this.itens = itens;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public InfoAdicional getInfoAdicional() {
        return infoAdicional;
    }

    public void setInfoAdicional(InfoAdicional infoAdicional) {
        this.infoAdicional = infoAdicional;
    }
}
