package school.sptech.KentoCafe.mapper;

import school.sptech.KentoCafe.dto.pedido.item.ItemResponse;
import school.sptech.KentoCafe.dto.pedido.pedido.PedidoResponse;
import school.sptech.KentoCafe.entity.Pedido;

import java.util.List;
import java.util.stream.Collectors;

public class PedidoMapper {

    public static PedidoResponse toResponse(Pedido pedido, Double total) {
        PedidoResponse resp = new PedidoResponse();
        resp.setId(pedido.getId());
        resp.setDataHora(pedido.getDtHrPedido());
        resp.setStatus(pedido.getStatus());
        resp.setObservacao(pedido.getInfoAdicional());
        resp.setValorTotal(total);

        List<ItemResponse> itensResp = pedido.getItens().stream().map(venda -> {
            ItemResponse item = new ItemResponse();
            item.setNomeProduto(venda.getProduto().getNome());
            item.setQuantidade(venda.getQuantidade());
            item.setPrecoUnitario(venda.getProduto().getPrecoUnidade());
            item.setSubtotal(venda.getQuantidade() * venda.getProduto().getPrecoUnidade());
            return item;
        }).collect(Collectors.toList());

        resp.setItens(itensResp);
        return resp;
    }
}
