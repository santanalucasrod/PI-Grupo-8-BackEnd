
package school.sptech.KentoCafe.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.KentoCafe.dto.pedido.item.ItemRequest;
import school.sptech.KentoCafe.dto.pedido.pedido.PedidoRequest;
import school.sptech.KentoCafe.dto.pedido.pedido.PedidoResponse;
import school.sptech.KentoCafe.entity.*;
import school.sptech.KentoCafe.mapper.PedidoMapper;
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
    private final ProdutoTamanhoRepository produtoTamanhoRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         StatusRepository statusRepository,
                         FuncionarioRepository funcionarioRepository,
                         ProdutoRepository produtoRepository,
                         PersonalizacaoRepository personalizacaoRepository,
                         ItemPedidoRepository itemPedidoRepository,
                         ProdutoTamanhoRepository produtoTamanhoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.statusRepository = statusRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.produtoRepository = produtoRepository;
        this.personalizacaoRepository = personalizacaoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.produtoTamanhoRepository = produtoTamanhoRepository;
    }

    // CRIAR PEDIDO
    @Transactional
    @CacheEvict(value = {"pedidos", "pedidosAtivos"}, allEntries = true)
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

            BigDecimal precoUnidade = definirTamanhoEPreco(item, produto, itemReq.getTamanhoId());

            item.setObservacao(itemReq.getObservacao());
            item.setPronto(false);

            if (itemReq.getPersonalizacaoIds() != null && !itemReq.getPersonalizacaoIds().isEmpty()) {
                List<Personalizacao> personalizacoes = personalizacaoRepository
                        .findAllById(itemReq.getPersonalizacaoIds());
                item.setPersonalizacoes(personalizacoes);
            }

            total = total.add(
                    precoUnidade.multiply(BigDecimal.valueOf(itemReq.getQuantidade()))
            );

            itens.add(item);
        }

        pedido.setItens(itens);
        pedido.setValorTotal(total);

        return pedidoRepository.save(pedido);
    }

    // CONCLUIR PEDIDO
    @Transactional
    @CacheEvict(value = {"pedidos", "pedidosAtivos"}, allEntries = true)
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

    // CANCELAR PEDIDO
    @Transactional
    @CacheEvict(value = {"pedidos", "pedidosAtivos"}, allEntries = true)
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

    // LISTAR TODOS
    @Transactional(readOnly = true)
    public List<PedidoResponse> listarTodos() {
        System.out.println("Buscando pedidos");
        List<Pedido> pedidos = pedidoRepository.findAll();

        // Converte para DTO aqui dentro, antes de salvar no cache
        return PedidoMapper.toResponseList(pedidos);
    }

    @Cacheable(value = "pedidosAtivos")
    @Transactional(readOnly = true)
    public List<PedidoResponse> listarAtivos() {
        System.out.println("Buscando pedidos ativos");

        List<Pedido> pedidos = pedidoRepository.findByStatusNomeIn(
                List.of("Pendente", "Em preparo")
        );

        return PedidoMapper.toResponseList(pedidos);
    }

    // LISTAR POR STATUS
    @Cacheable(value = "pedidos", key = "'status:' + #statusNome")
    @Transactional(readOnly = true)
    public List<PedidoResponse> listarPorStatus(String statusNome) {
        System.out.println("Listar por status");
        List<Pedido> pedidos = pedidoRepository.findByStatusNome(statusNome);

        return PedidoMapper.toResponseList(pedidos);
    }

    // BUSCAR PEDIDO POR ID
    @Cacheable(value = "pedidos", key = "'id:' + #id")
    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) {
        System.out.println("Buscar 1 pedido");
        Pedido pedido = buscarOuLancarErro(id);

        return PedidoMapper.toResponse(pedido);
    }

    // ATUALIZAR STATUS
    @Transactional
    @CacheEvict(value = {"pedidos", "pedidosAtivos"}, allEntries = true)
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

    // MARCAR ITEM COMO PRONTO
    @Transactional
    @CacheEvict(value = {"pedidos", "pedidosAtivos"}, allEntries = true)
    public ItemPedido marcarItemPronto(Long itemId, boolean pronto) {
        ItemPedido item = itemPedidoRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Item não encontrado"));

        item.setPronto(pronto);

        return itemPedidoRepository.save(item);
    }

    // DEFINIR TAMANHO E PREÇO
    // Produtos com tamanhos cadastrados (bebidas) usam o preço do tamanho escolhido;
    // os demais usam o preço do próprio produto.
    private BigDecimal definirTamanhoEPreco(ItemPedido item, Produto produto, Long tamanhoId) {
        BigDecimal preco;

        if (tamanhoId != null) {
            ProdutoTamanho produtoTamanho = produtoTamanhoRepository
                    .findByProdutoIdAndTamanhoId(produto.getId(), tamanhoId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Tamanho não disponível para o produto: " + produto.getNome()));

            item.setTamanho(produtoTamanho.getTamanho());
            preco = produtoTamanho.getPrecoUnidade();

        } else {
            if (!produtoTamanhoRepository.findByProdutoId(produto.getId()).isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Escolha o tamanho do produto: " + produto.getNome());
            }

            preco = produto.getPrecoUnidade();
        }

        if (preco == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Produto sem preço cadastrado: " + produto.getNome());
        }

        item.setPrecoUnidade(preco);
        return preco;
    }

    // CONVERTER STATUS DO FRONT
    private Status buscarStatusPorNomeFront(String statusFront) {
        String alvo = statusFront == null ? "" : statusFront.trim().toUpperCase().replace(" ", "_");

        return statusRepository.findAll().stream()
                .filter(s -> s.getNome().trim().toUpperCase().replace(" ", "_").equals(alvo))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Status inválido: " + statusFront));
    }

    // BUSCAR PEDIDO OU LANÇAR ERRO
    private Pedido buscarOuLancarErro(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pedido não encontrado"));
    }
}
