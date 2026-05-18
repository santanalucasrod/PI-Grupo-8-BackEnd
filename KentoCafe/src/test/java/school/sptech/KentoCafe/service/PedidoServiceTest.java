package school.sptech.KentoCafe.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.KentoCafe.dto.pedido.item.ItemRequest;
import school.sptech.KentoCafe.dto.pedido.pedido.PedidoRequest;
import school.sptech.KentoCafe.dto.pedido.pedido.PedidoResponse;
import school.sptech.KentoCafe.entity.Funcionario;
import school.sptech.KentoCafe.entity.InfoAdicional;
import school.sptech.KentoCafe.entity.Pedido;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.exception.CarrinhoVazioException;
import school.sptech.KentoCafe.exception.EntidadeNaoEncontradoException;
import school.sptech.KentoCafe.exception.ProdutoNaoEncontradoException;
import school.sptech.KentoCafe.repository.FuncionarioRepository;
import school.sptech.KentoCafe.repository.InfoAdicionalRepository;
import school.sptech.KentoCafe.repository.PedidoRepository;
import school.sptech.KentoCafe.repository.ProdutoRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private InfoAdicionalRepository infoAdicionalRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    @DisplayName("Deve finalizar o pedido com sucesso quando todos os dados forem válidos")
    void deveFinalizarPedidoComSucesso() {
        // Arrange
        PedidoRequest request = criarPedidoRequestValido();
        Funcionario funcionarioMock = request.getFuncionario();
        InfoAdicional infoMock = request.getInfoAdicional();

        Produto produtoMock = new Produto();
        produtoMock.setId(1);
        produtoMock.setPrecoUnidade(10.0);

        when(funcionarioRepository.findById(funcionarioMock.getId())).thenReturn(Optional.of(funcionarioMock));
        when(infoAdicionalRepository.findById(infoMock.getId())).thenReturn(Optional.of(infoMock));
        when(produtoRepository.findById(1)).thenReturn(Optional.of(produtoMock));

        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido pedido = invocation.getArgument(0);
            pedido.setId(123);
            return pedido;
        });

        // Act
        PedidoResponse response = pedidoService.finalizarPedido(request);

        // Assert
        assertNotNull(response);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        verify(funcionarioRepository, times(1)).findById(funcionarioMock.getId());
        verify(infoAdicionalRepository, times(1)).findById(infoMock.getId());
        verify(produtoRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Deve lançar CarrinhoVazioException quando a lista de itens for nula")
    void deveLancarExceptionQuandoItensForNulo() {
        PedidoRequest request = new PedidoRequest();
        request.setItens(null);

        assertThrows(CarrinhoVazioException.class, () -> pedidoService.finalizarPedido(request));
        verifyNoInteractions(funcionarioRepository, infoAdicionalRepository, produtoRepository, pedidoRepository);
    }

    @Test
    @DisplayName("Deve lançar CarrinhoVazioException quando a lista de itens estiver vazia")
    void deveLancarExceptionQuandoItensEstiverVazio() {
        PedidoRequest request = new PedidoRequest();
        request.setItens(Collections.emptyList());

        assertThrows(CarrinhoVazioException.class, () -> pedidoService.finalizarPedido(request));
        verifyNoInteractions(funcionarioRepository, infoAdicionalRepository, produtoRepository, pedidoRepository);
    }

    @Test
    @DisplayName("Deve lançar EntidadeNaoEncontradoException quando o funcionário ou seu ID for nulo")
    void deveLancarExceptionQuandoFuncionarioIdForNulo() {
        PedidoRequest request = new PedidoRequest();
        request.setItens(List.of(new ItemRequest()));
        request.setFuncionario(null);

        EntidadeNaoEncontradoException exception = assertThrows(EntidadeNaoEncontradoException.class,
                () -> pedidoService.finalizarPedido(request));

        assertEquals("O funcionário responsável pelo pedido é obrigatório.", exception.getMessage());
        verifyNoInteractions(funcionarioRepository);
    }

    @Test
    @DisplayName("Deve lançar EntidadeNaoEncontradoException quando o funcionário não existir no banco")
    void deveLancarExceptionQuandoFuncionarioNaoEncontradoNoBanco() {
        PedidoRequest request = criarPedidoRequestValido();

        when(funcionarioRepository.findById(request.getFuncionario().getId())).thenReturn(Optional.empty());

        EntidadeNaoEncontradoException exception = assertThrows(EntidadeNaoEncontradoException.class,
                () -> pedidoService.finalizarPedido(request));

        assertEquals("Funcionário não encontrado", exception.getMessage());
        verifyNoInteractions(infoAdicionalRepository, produtoRepository, pedidoRepository);
    }

    @Test
    @DisplayName("Deve lançar EntidadeNaoEncontradoException quando a infoAdicional ou seu ID for nulo")
    void deveLancarExceptionQuandoInfoAdicionalIdForNulo() {
        PedidoRequest request = new PedidoRequest();
        request.setItens(List.of(new ItemRequest()));

        Funcionario f = new Funcionario();
        f.setId(1);
        request.setFuncionario(f);
        request.setInfoAdicional(null);

        when(funcionarioRepository.findById(f.getId())).thenReturn(Optional.of(f));

        EntidadeNaoEncontradoException exception = assertThrows(EntidadeNaoEncontradoException.class,
                () -> pedidoService.finalizarPedido(request));

        assertEquals("A informação adicional é obrigatória.", exception.getMessage());
        verifyNoInteractions(infoAdicionalRepository);
    }

    @Test
    @DisplayName("Deve lançar EntidadeNaoEncontradoException quando a infoAdicional não existir no banco")
    void deveLancarExceptionQuandoInfoAdicionalNaoEncontradaNoBanco() {
        PedidoRequest request = criarPedidoRequestValido();
        Funcionario f = request.getFuncionario();

        when(funcionarioRepository.findById(f.getId())).thenReturn(Optional.of(f));
        when(infoAdicionalRepository.findById(request.getInfoAdicional().getId())).thenReturn(Optional.empty());

        EntidadeNaoEncontradoException exception = assertThrows(EntidadeNaoEncontradoException.class,
                () -> pedidoService.finalizarPedido(request));

        assertEquals("Informação adicional não encontrada no banco.", exception.getMessage());
        verifyNoInteractions(produtoRepository, pedidoRepository);
    }

    @Test
    @DisplayName("Deve lançar EntidadeNaoEncontradoException quando algum produto do item não existir")
    void deveLancarExceptionQuandoProdutoNaoExistir() {
        PedidoRequest request = criarPedidoRequestValido();
        Funcionario f = request.getFuncionario();
        InfoAdicional info = request.getInfoAdicional();

        when(funcionarioRepository.findById(f.getId())).thenReturn(Optional.of(f));
        when(infoAdicionalRepository.findById(info.getId())).thenReturn(Optional.of(info));

        when(produtoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ProdutoNaoEncontradoException.class, () -> pedidoService.finalizarPedido(request));
        verifyNoInteractions(pedidoRepository);
    }

    private PedidoRequest criarPedidoRequestValido() {
        PedidoRequest request = new PedidoRequest();
        request.setNome("Cliente Teste");

        Funcionario f = new Funcionario();
        f.setId(99);
        request.setFuncionario(f);

        InfoAdicional info = new InfoAdicional();
        info.setId(5);
        info.setDescricao("Sem açúcar");
        request.setInfoAdicional(info);

        ItemRequest item = new ItemRequest();
        item.setProdutoId(1);
        item.setQuantidade(2);

        List<ItemRequest> itens = new ArrayList<>();
        itens.add(item);
        request.setItens(itens);

        return request;
    }
}