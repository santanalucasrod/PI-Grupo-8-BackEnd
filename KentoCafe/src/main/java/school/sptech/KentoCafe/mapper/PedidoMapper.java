package school.sptech.KentoCafe.mapper;

import school.sptech.KentoCafe.dto.pedido.item.ItemResponse;
import school.sptech.KentoCafe.dto.pedido.pedido.PedidoResponse;
import school.sptech.KentoCafe.entity.Pedido;
import school.sptech.KentoCafe.entity.Personalizacao;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoMapper {

    public static PedidoResponse toResponse(Pedido pedido) {
        PedidoResponse resp = new PedidoResponse();
        resp.setId(pedido.getId());
        resp.setNomeCliente(pedido.getNomeCliente());
        resp.setDtHrPedido(pedido.getDtHrPedido());
        resp.setDtHrPronto(pedido.getDtHrPronto());
        resp.setStatus(pedido.getStatus().getNome());
        resp.setValorTotal(pedido.getValorTotal());

        List<ItemResponse> itensResp = pedido.getItens().stream()
                .map(item -> {
                    ItemResponse itemResp = new ItemResponse();
                    itemResp.setNomeProduto(item.getProduto().getNome());
                    itemResp.setQuantidade(item.getQuantidade());
                    itemResp.setPrecoUnitario(item.getPrecoUnidade());

                    BigDecimal subtotal = item.getPrecoUnidade()
                            .multiply(BigDecimal.valueOf(item.getQuantidade()));
                    itemResp.setSubtotal(subtotal);

                    if (item.getPersonalizacoes() != null && !item.getPersonalizacoes().isEmpty()) {
                        List<String> personalizacoes = item.getPersonalizacoes()
                                .stream()
                                .map(Personalizacao::getNome)
                                .collect(Collectors.toList());
                        itemResp.setPersonalizacoes(personalizacoes);
                    }

                    return itemResp;
                })
                .collect(Collectors.toList());

        resp.setItens(itensResp);
        return resp;
    }

    public static List<PedidoResponse> toResponseList(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(PedidoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
