package school.sptech.KentoCafe.dto.pedido.pedido;

import school.sptech.KentoCafe.dto.pedido.item.ItemResponse;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponse {
    private Integer id;
    private LocalDateTime dataHora;
    private String status;
    private Double valorTotal;
    private String observacao;
    private List<ItemResponse> itens;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public List<ItemResponse> getItens() {
        return itens;
    }

    public void setItens(List<ItemResponse> itens) {
        this.itens = itens;
    }
}
