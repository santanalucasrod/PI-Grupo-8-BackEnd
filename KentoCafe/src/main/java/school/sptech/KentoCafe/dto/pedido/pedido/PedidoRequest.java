package school.sptech.KentoCafe.dto.pedido.pedido;

import school.sptech.KentoCafe.dto.pedido.item.ItemRequest;

import java.util.List;

public class PedidoRequest {
    private String infoAdicional;
    private List<ItemRequest> itens;

    public String getInfoAdicional() {
        return infoAdicional;
    }

    public void setInfoAdicional(String infoAdicional) {
        this.infoAdicional = infoAdicional;
    }

    public List<ItemRequest> getItens() {
        return itens;
    }

    public void setItens(List<ItemRequest> itens) {
        this.itens = itens;
    }
}
