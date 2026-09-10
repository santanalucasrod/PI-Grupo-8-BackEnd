package school.sptech.KentoCafe.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.KentoCafe.dto.pedido.item.ItemRequest;
import school.sptech.KentoCafe.dto.pedido.pedido.PedidoRequest;
import school.sptech.KentoCafe.entity.*;
import school.sptech.KentoCafe.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final StatusRepository statusRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ProdutoRepository produtoRepository;
    private final PersonalizacaoRepository personalizacaoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         StatusRepository statusRepository,
                         FuncionarioRepository funcionarioRepository,
                         ProdutoRepository produtoRepository,
                         PersonalizacaoRepository personalizacaoRepository,
                         ItemPedidoRepository itemPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.statusRepository = statusRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.produtoRepository = produtoRepository;
        this.personalizacaoRepository = personalizacaoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @Transactional
    public Pedido criar(PedidoRequest request) {

        Status pendente = statusRepository.findByNome("Pendente")
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Status 'Pendente' não encontrado"));

        Funcionario funcionario = funcionarioRepository.findById(request.getFuncionarioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Funcionário não encontrado"));

        Pedido pedido = new Pedido();
        pedido.setNomeCliente(request.getNomeCliente());
        pedido.setDtHrPedido(LocalDateTime.now());
        pedido.setDtHrPronto(null);
        pedido.setDescricao(request.getDescricao());
        pedido.setStatus(pendente);
        pedido.setFuncionario(funcionario);

        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ItemRequest itemReq : request.getItens()) {

            Produto produto = produtoRepository.findById(itemReq.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Produto não encontrado: id " + itemReq.getProdutoId()));

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemReq.getQuantidade());
            item.setPrecoUnidade(produto.getPrecoUnidade());
            item.setObservacao(itemReq.getObservacao());
            item.setPronto(false);

            if (itemReq.getPersonalizacaoIds() != null && !itemReq.getPersonalizacaoIds().isEmpty()) {
                List<Personalizacao> personalizacoes = personalizacaoRepository
                        .findAllById(itemReq.getPersonalizacaoIds());
                item.setPersonalizacoes(personalizacoes);
            }

            total = total.add(
                    produto.getPrecoUnidade().multiply(BigDecimal.valueOf(itemReq.getQuantidade()))
            );

            itens.add(item);
        }

        pedido.setItens(itens);
        pedido.setValorTotal(total);

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido concluir(Long id) {
        Pedido pedido = buscarOuLancarErro(id);

        if (pedido.getStatus().getNome().equals("Cancelado")) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Pedido cancelado não pode ser concluído");
        }

        if (pedido.getStatus().getNome().equals("Pronto")) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Pedido já está pronto");
        }

        Status pronto = statusRepository.findByNome("Pronto")
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Status 'Pronto' não encontrado"));

        pedido.setStatus(pronto);
        pedido.setDtHrPronto(LocalDateTime.now());

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido cancelar(Long id) {
        Pedido pedido = buscarOuLancarErro(id);

        if (pedido.getStatus().getNome().equals("Pronto")) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Pedido já concluído não pode ser cancelado");
        }

        if (pedido.getStatus().getNome().equals("Cancelado")) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Pedido já está cancelado");
        }

        Status cancelado = statusRepository.findByNome("Cancelado")
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Status 'Cancelado' não encontrado"));

        pedido.setStatus(cancelado);

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarAtivos() {
        return pedidoRepository.findByStatusNomeIn(List.of("Pendente", "Em preparo"));
    }

    public List<Pedido> listarPorStatus(String statusNome) {
        return pedidoRepository.findByStatusNome(statusNome);
    }

    public Pedido buscarPorId(Long id) {
        return buscarOuLancarErro(id);
    }

    @Transactional
    public Pedido atualizarStatus(Long id, String statusFront) {
        Pedido pedido = buscarOuLancarErro(id);

        if (pedido.getStatus().getNome().equals("Pronto")
                || pedido.getStatus().getNome().equals("Cancelado")) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Pedido já está pronto ou foi cancelado");
        }

        Status novoStatus = buscarStatusPorNomeFront(statusFront);
        pedido.setStatus(novoStatus);

        if (novoStatus.getNome().equals("Pronto")) {
            pedido.setDtHrPronto(LocalDateTime.now());
        }

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public ItemPedido marcarItemPronto(Long itemId, boolean pronto) {
        ItemPedido item = itemPedidoRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Item não encontrado"));

        item.setPronto(pronto);
        return itemPedidoRepository.save(item);
    }

    // Converte o status enviado pelo front (ex.: "EM_PREPARO") para o nome salvo no banco (ex.: "Em preparo")
    private Status buscarStatusPorNomeFront(String statusFront) {
        String alvo = statusFront == null ? "" : statusFront.trim().toUpperCase().replace(" ", "_");
        return statusRepository.findAll().stream()
                .filter(s -> s.getNome().trim().toUpperCase().replace(" ", "_").equals(alvo))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Status inválido: " + statusFront));
    }

    private Pedido buscarOuLancarErro(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pedido não encontrado"));
    }
}
