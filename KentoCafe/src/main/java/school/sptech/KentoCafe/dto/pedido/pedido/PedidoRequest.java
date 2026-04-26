package school.sptech.KentoCafe.dto.pedido.pedido;

import io.swagger.v3.oas.annotations.media.Schema;
import school.sptech.KentoCafe.dto.pedido.item.ItemRequest;

import java.util.List;

@Schema(description = "Dados para criação de um pedido")
public class PedidoRequest {
    @Schema(description = "Informações ou observações adicionais do pedido", example = "Sem açúcar")
    private String infoAdicional;

    @Schema(description = "Lista de itens do pedido")
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
