package school.sptech.KentoCafe.dto.pedido.InfoAdicional;

public class InfoAdicionalRequest {
    private Integer PedidoId;
    private String descricao;
    private String preferenciaIndividual;

    public InfoAdicionalRequest(){

    }

    public InfoAdicionalRequest(Integer pedidoId, String descricao, String preferenciaIndividual) {
        PedidoId = pedidoId;
        this.descricao = descricao;
        this.preferenciaIndividual = preferenciaIndividual;
    }

    public Integer getPedidoId() {
        return PedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        PedidoId = pedidoId;
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
}
