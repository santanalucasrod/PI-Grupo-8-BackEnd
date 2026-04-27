package school.sptech.KentoCafe.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import school.sptech.KentoCafe.dto.pedido.item.ItemRequest;
import school.sptech.KentoCafe.dto.pedido.pedido.PedidoRequest;
import school.sptech.KentoCafe.dto.pedido.pedido.PedidoResponse;
import school.sptech.KentoCafe.entity.Pedido;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.entity.Venda;
import school.sptech.KentoCafe.exception.CarrinhoVazioException;
import school.sptech.KentoCafe.exception.ProdutoNaoEncontradoException;
import school.sptech.KentoCafe.mapper.PedidoMapper;
import school.sptech.KentoCafe.repository.PedidoRepository;
import school.sptech.KentoCafe.repository.ProdutoRepository;

import java.time.LocalDateTime;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public PedidoResponse finalizarPedido(PedidoRequest request) {
        if (request.getItens() == null || request.getItens().isEmpty()) {
            throw new CarrinhoVazioException();
        }

        Pedido novoPedido = new Pedido();
        novoPedido.setDtHrPedido(LocalDateTime.now());
        novoPedido.setDtHrPronto(LocalDateTime.now());
        novoPedido.setStatus("PENDENTE");
        novoPedido.setInfoAdicional(request.getInfoAdicional());

        double totalAcumulado = 0.0;

        for (ItemRequest item : request.getItens()) {
            Produto p = produtoRepository.findById(item.getProdutoId())
                    .orElseThrow(ProdutoNaoEncontradoException::new);

            Venda venda = new Venda();
            venda.setProduto(p);
            venda.setQuantidade(item.getQuantidade());
            venda.setPedido(novoPedido);

            totalAcumulado += p.getPrecoUnidade() * item.getQuantidade();

            novoPedido.getItens().add(venda);
        }

        Pedido pedidoSalvo = pedidoRepository.save(novoPedido);
        return PedidoMapper.toResponse(pedidoSalvo, totalAcumulado);
    }

}
