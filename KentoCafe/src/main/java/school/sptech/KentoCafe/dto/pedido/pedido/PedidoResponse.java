package school.sptech.KentoCafe.dto.pedido.pedido;

import school.sptech.KentoCafe.dto.pedido.item.ItemResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponse {
    private Long id;
    private String nomeCliente;
    private LocalDateTime dtHrPedido;
    private LocalDateTime dtHrPronto;
    private String status;
    private BigDecimal valorTotal;
    private List<ItemResponse> itens;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
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

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemResponse> getItens() {
        return itens;
    }

    public void setItens(List<ItemResponse> itens) {
        this.itens = itens;
    }
}
