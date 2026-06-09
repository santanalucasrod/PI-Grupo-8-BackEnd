package school.sptech.KentoCafe.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.KentoCafe.dto.pedido.item.ItemRequest;
import school.sptech.KentoCafe.dto.pedido.pedido.PedidoRequest;
import school.sptech.KentoCafe.entity.*;
import school.sptech.KentoCafe.repository.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import org.junit.jupiter.api.Nested;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock private PedidoRepository pedidoRepository;
    @Mock private StatusRepository statusRepository;
    @Mock private FuncionarioRepository funcionarioRepository;
    @Mock private ProdutoRepository produtoRepository;
    @Mock private PersonalizacaoRepository personalizacaoRepository;

    @InjectMocks private PedidoService pedidoService;

    @Nested
    @DisplayName("Cenários do método criar")
    class CriarPedidoTests {

        @Test
        @DisplayName("Deve lançar NOT_FOUND quando status 'Em preparo' não existir (Cenário 1.1)")
        void statusNaoEncontrado() {
            PedidoRequest request = new PedidoRequest();
            when(statusRepository.findByNome("Em preparo")).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> pedidoService.criar(request));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            assertTrue(ex.getReason().contains("Status 'Em preparo'"));
        }

        @Test
        @DisplayName("Deve lançar NOT_FOUND quando funcionário não existir (Cenário 1.2)")
        void funcionarioNaoEncontrado() {
            PedidoRequest request = new PedidoRequest();
            request.setFuncionarioId(1L);

            when(statusRepository.findByNome("Em preparo")).thenReturn(Optional.of(new Status("Em preparo")));
            when(funcionarioRepository.findById(1L)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> pedidoService.criar(request));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        }

        @Test
        @DisplayName("Deve lançar NOT_FOUND quando algum produto do item não existir (Cenário 1.3)")
        void produtoNaoEncontrado() {
            PedidoRequest request = new PedidoRequest();
            request.setFuncionarioId(1L);

            ItemRequest item = new ItemRequest();
            item.setProdutoId(50L);
            request.setItens(List.of(item));

            when(statusRepository.findByNome("Em preparo")).thenReturn(Optional.of(new Status("Em preparo")));
            when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(new Funcionario()));
            when(produtoRepository.findById(50L)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> pedidoService.criar(request));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            assertTrue(ex.getReason().contains("Produto não encontrado"));
        }

        @Test
        @DisplayName("Deve criar pedido com sucesso sem personalizações e calcular valor total (Cenário 1.4)")
        void criarComSucessoSemPersonalizacao() {
            PedidoRequest request = new PedidoRequest();
            request.setFuncionarioId(1L);
            request.setNomeCliente("Murilo");

            ItemRequest itemReq = new ItemRequest();
            itemReq.setProdutoId(10L);
            itemReq.setQuantidade(2);
            itemReq.setPersonalizacaoIds(null);
            request.setItens(List.of(itemReq));

            Produto produto = new Produto();
            produto.setId(10L);
            produto.setPrecoUnidade(new BigDecimal("15.50"));

            when(statusRepository.findByNome("Em preparo")).thenReturn(Optional.of(new Status("Em preparo")));
            when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(new Funcionario()));
            when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));
            when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));

            Pedido resultado = pedidoService.criar(request);

            assertNotNull(resultado);
            assertEquals("Murilo", resultado.getNomeCliente());
            assertEquals(new BigDecimal("31.00"), resultado.getValorTotal());
            assertEquals(1, resultado.getItens().size());
            verify(personalizacaoRepository, never()).findAllById(any());
        }

        @Test
        @DisplayName("criar: Deve associar personalizações quando a lista de IDs contiver elementos")
        void criarComPersonalizacoes() {
            // Arrange
            PedidoRequest request = new PedidoRequest();
            request.setFuncionarioId(1L);

            ItemRequest itemReq = new ItemRequest();
            itemReq.setProdutoId(10L);
            itemReq.setQuantidade(1);
            itemReq.setPersonalizacaoIds(List.of(1L, 2L));
            request.setItens(List.of(itemReq));

            Status status = new Status();
            Funcionario funcionario = new Funcionario();
            Produto produto = new Produto();
            produto.setPrecoUnidade(BigDecimal.TEN);

            Personalizacao p1 = new Personalizacao();
            Personalizacao p2 = new Personalizacao();

            when(statusRepository.findByNome("Em preparo")).thenReturn(Optional.of(status));
            when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
            when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));

            when(personalizacaoRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(p1, p2));
            when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));

            // Act
            Pedido resultado = pedidoService.criar(request);

            // Assert
            assertNotNull(resultado);
            verify(personalizacaoRepository, times(1)).findAllById(List.of(1L, 2L));
        }

        @Test
        @DisplayName("Deve criar pedido com sucesso aplicando as personalizações (Cenário 1.5)")
        void criarComSucessoComPersonalizacao() {
            PedidoRequest request = new PedidoRequest();
            request.setFuncionarioId(1L);

            ItemRequest itemReq = new ItemRequest();
            itemReq.setProdutoId(10L);
            itemReq.setQuantidade(1);
            itemReq.setPersonalizacaoIds(List.of(1L, 2L));
            request.setItens(List.of(itemReq));

            Produto produto = new Produto();
            produto.setId(10L);
            produto.setPrecoUnidade(new BigDecimal("10.00"));

            List<Personalizacao> perms = List.of(new Personalizacao(), new Personalizacao());

            when(statusRepository.findByNome("Em preparo")).thenReturn(Optional.of(new Status("Em preparo")));
            when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(new Funcionario()));
            when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));
            when(personalizacaoRepository.findAllById(List.of(1L, 2L))).thenReturn(perms);
            when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));

            Pedido resultado = pedidoService.criar(request);

            assertNotNull(resultado);
            assertEquals(perms, resultado.getItens().getFirst().getPersonalizacoes());
        }

        @Test
        @DisplayName("criar: Não deve buscar personalizações quando a lista de IDs for nula")
        void criarComPersonalizacoesNula() {
            // Arrange
            PedidoRequest request = new PedidoRequest();
            request.setFuncionarioId(1L);

            ItemRequest itemReq = new ItemRequest();
            itemReq.setProdutoId(10L);
            itemReq.setQuantidade(1);
            itemReq.setPersonalizacaoIds(null);
            request.setItens(List.of(itemReq));

            Status status = new Status();
            Funcionario funcionario = new Funcionario();
            Produto produto = new Produto();
            produto.setPrecoUnidade(BigDecimal.TEN);

            when(statusRepository.findByNome("Em preparo")).thenReturn(Optional.of(status));
            when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
            when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));
            when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));

            // Act
            Pedido resultado = pedidoService.criar(request);

            // Assert
            assertNotNull(resultado);
            verifyNoInteractions(personalizacaoRepository);
        }

        @Test
        @DisplayName("criar: Não deve buscar personalizações quando a lista de IDs estiver vazia")
        void criarComPersonalizacoesVazia() {
            // Arrange
            PedidoRequest request = new PedidoRequest();
            request.setFuncionarioId(1L);

            ItemRequest itemReq = new ItemRequest();
            itemReq.setProdutoId(10L);
            itemReq.setQuantidade(1);
            // Passo crucial: Lista iniciada, mas vazia
            itemReq.setPersonalizacaoIds(Collections.emptyList());
            request.setItens(List.of(itemReq));

            Status status = new Status();
            Funcionario funcionario = new Funcionario();
            Produto produto = new Produto();
            produto.setPrecoUnidade(BigDecimal.TEN);

            when(statusRepository.findByNome("Em preparo")).thenReturn(Optional.of(status));
            when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
            when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));
            when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));

            // Act
            Pedido resultado = pedidoService.criar(request);

            // Assert
            assertNotNull(resultado);
            verifyNoInteractions(personalizacaoRepository);
        }
    }

    @Nested
    @DisplayName("Cenários do método concluir")
    class ConcluirPedidoTests {

        @Test
        @DisplayName("Deve lançar NOT_FOUND se o pedido não existir (Cenário 2.1)")
        void pedidoInexistente() {
            when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(ResponseStatusException.class, () -> pedidoService.concluir(1L));
        }

        @Test
        @DisplayName("Deve lançar CONFLICT se o pedido já estiver cancelado (Cenário 2.2)")
        void pedidoCancelado() {
            Pedido pedido = new Pedido();
            pedido.setStatus(new Status("Cancelado"));

            when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> pedidoService.concluir(1L));
            assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        }

        @Test
        @DisplayName("Deve lançar CONFLICT se o pedido já estiver pronto (Cenário 2.3)")
        void pedidoJaPronto() {
            Pedido pedido = new Pedido();
            pedido.setStatus(new Status("Pronto"));

            when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> pedidoService.concluir(1L));
            assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        }

        @Test
        @DisplayName("Deve lançar NOT_FOUND se o status 'Pronto' sumir do banco (Cenário 2.4)")
        void statusProntoSumiu() {
            Pedido pedido = new Pedido();
            pedido.setStatus(new Status("Em preparo"));

            when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
            when(statusRepository.findByNome("Pronto")).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> pedidoService.concluir(1L));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        }

        @Test
        @DisplayName("Deve concluir o pedido com sucesso (Cenário 2.5)")
        void concluirComSucesso() {
            Pedido pedido = new Pedido();
            pedido.setStatus(new Status("Em preparo"));
            Status statusPronto = new Status("Pronto");

            when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
            when(statusRepository.findByNome("Pronto")).thenReturn(Optional.of(statusPronto));
            when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));

            Pedido resultado = pedidoService.concluir(1L);

            assertEquals(statusPronto, resultado.getStatus());
            assertNotNull(resultado.getDtHrPronto());
        }
    }

    @Nested
    @DisplayName("Cenários do método cancelar")
    class CancelarPedidoTests {

        @Test
        @DisplayName("Deve lançar NOT_FOUND se o pedido não existir (Cenário 3.1)")
        void pedidoInexistente() {
            when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(ResponseStatusException.class, () -> pedidoService.cancelar(1L));
        }

        @Test
        @DisplayName("Deve lançar CONFLICT se o pedido já estiver pronto (Cenário 3.2)")
        void pedidoJaPronto() {
            Pedido pedido = new Pedido();
            pedido.setStatus(new Status("Pronto"));

            when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> pedidoService.cancelar(1L));
            assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        }

        @Test
        @DisplayName("Deve lançar CONFLICT se o pedido já estiver cancelado (Cenário 3.3)")
        void pedidoJaCancelado() {
            Pedido pedido = new Pedido();
            pedido.setStatus(new Status("Cancelado"));

            when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> pedidoService.cancelar(1L));
            assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        }

        @Test
        @DisplayName("Deve lançar NOT_FOUND se o status 'Cancelado' sumir do banco (Cenário 3.4)")
        void statusCanceladoSumiu() {
            Pedido pedido = new Pedido();
            pedido.setStatus(new Status("Em preparo"));

            when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
            when(statusRepository.findByNome("Cancelado")).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> pedidoService.cancelar(1L));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        }

        @Test
        @DisplayName("Deve cancelar o pedido com sucesso (Cenário 3.5)")
        void cancelarComSucesso() {
            Pedido pedido = new Pedido();
            pedido.setStatus(new Status("Em preparo"));
            Status statusCancelado = new Status("Cancelado");

            when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
            when(statusRepository.findByNome("Cancelado")).thenReturn(Optional.of(statusCancelado));
            when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));

            Pedido resultado = pedidoService.cancelar(1L);

            assertEquals(statusCancelado, resultado.getStatus());
        }
    }

    @Nested
    @DisplayName("Cenários de consultas gerais")
    class ConsultasTests {

        @Test
        @DisplayName("Deve listar todos os pedidos (Cenário 4.1)")
        void listarTodos() {
            List<Pedido> lista = List.of(new Pedido(), new Pedido());
            when(pedidoRepository.findAll()).thenReturn(lista);

            List<Pedido> resultado = pedidoService.listarTodos();
            assertEquals(2, resultado.size());
        }

        @Test
        @DisplayName("Deve listar pedidos filtrando por nome do status (Cenário 4.2)")
        void listarPorStatus() {
            List<Pedido> lista = List.of(new Pedido());
            when(pedidoRepository.findByStatusNome("Pronto")).thenReturn(lista);

            List<Pedido> resultado = pedidoService.listarPorStatus("Pronto");
            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Deve buscar pedido por ID com sucesso (Cenário 4.3)")
        void buscarPorIdComSucesso() {
            Pedido pedido = new Pedido();
            when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

            Pedido resultado = pedidoService.buscarPorId(1L);
            assertNotNull(resultado);
        }
    }
}