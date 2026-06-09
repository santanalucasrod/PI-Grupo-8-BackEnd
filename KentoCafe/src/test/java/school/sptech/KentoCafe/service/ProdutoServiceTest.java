package school.sptech.KentoCafe.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.KentoCafe.dto.produto.ProdutoRequest;
import school.sptech.KentoCafe.dto.produto.ProdutoResponse;
import school.sptech.KentoCafe.entity.Categoria;
import school.sptech.KentoCafe.entity.Ingrediente;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.repository.CategoriaRepository;
import school.sptech.KentoCafe.repository.IngredienteRepository;
import school.sptech.KentoCafe.repository.ItemPedidoRepository;
import school.sptech.KentoCafe.repository.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock private ProdutoRepository produtoRepository;
    @Mock private CategoriaRepository categoriaRepository;
    @Mock private IngredienteRepository ingredienteRepository;
    @Mock private ItemPedidoRepository itemPedidoRepository;

    @InjectMocks private ProdutoService produtoService;

    private Categoria criarCategoria(Long id, String nome) {
        Categoria c = new Categoria();
        c.setId(id);
        c.setNome(nome);
        return c;
    }

    private Produto criarProduto(Long id, String nome, Categoria categoria) {
        Produto p = new Produto();
        p.setId(id);
        p.setNome(nome);
        p.setCategoria(categoria);
        p.setPrecoUnidade(BigDecimal.TEN);
        p.setIngredientes(new ArrayList<>());
        return p;
    }

    private ProdutoRequest criarRequest(Long categoriaId, String nome) {
        ProdutoRequest req = new ProdutoRequest();
        ProdutoRequest.Categoria catReq = new ProdutoRequest.Categoria();
        catReq.setId(categoriaId);
        req.setCategoria(catReq);
        req.setNome(nome);
        req.setPrecoUnidade(BigDecimal.TEN);
        return req;
    }

    @Nested
    @DisplayName("Cenários do método criar")
    class CriarTests {

        @Test
        @DisplayName("Deve lançar NOT_FOUND quando a categoria informada não existir")
        void categoriaNaoEncontrada() {
            ProdutoRequest request = criarRequest(1L, "Pizza");
            when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> produtoService.criar(request));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            assertEquals("Categoria não encontrada", ex.getReason());
        }

        @Test
        @DisplayName("Deve criar e retornar o produto quando a categoria existir")
        void criarComSucesso() {
            ProdutoRequest request = criarRequest(1L, "Pizza");
            Categoria categoria = criarCategoria(1L, "Comidas");
            Produto produtoSalvo = criarProduto(10L, "Pizza", categoria);

            when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
            when(produtoRepository.save(any(Produto.class))).thenReturn(produtoSalvo);

            ProdutoResponse resultado = produtoService.criar(request);

            assertNotNull(resultado);
            verify(produtoRepository, times(1)).save(any(Produto.class));
        }
    }

    @Nested
    @DisplayName("Cenários de Listagem e Busca Simples")
    class ConsultasSimplesTests {

        @Test
        @DisplayName("Deve listar todos os produtos cadastrados")
        void listarTodos() {
            Categoria cat = criarCategoria(1L, "Bebidas");
            List<Produto> produtos = List.of(criarProduto(1L, "Coca-Cola", cat));
            when(produtoRepository.findAll()).thenReturn(produtos);

            List<ProdutoResponse> resultado = produtoService.listarTodos();

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Deve buscar produto por ID com sucesso")
        void buscarPorIdComSucesso() {
            Categoria cat = criarCategoria(1L, "Bebidas");
            Produto produto = criarProduto(1L, "Coca-Cola", cat);
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

            ProdutoResponse resultado = produtoService.buscarPorId(1L);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Deve lançar NOT_FOUND ao buscar por um ID de produto inexistente")
        void buscarPorIdInexistente() {
            when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> produtoService.buscarPorId(99L));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        }

        @Test
        @DisplayName("Deve listar produtos por ID de categoria")
        void listarPorCategoria() {
            Categoria cat = criarCategoria(1L, "Doces");
            when(produtoRepository.findByCategoriaId(1L)).thenReturn(List.of(criarProduto(2L, "Chocolate", cat)));

            List<ProdutoResponse> resultado = produtoService.listarPorCategoria(1L);

            assertEquals(1, resultado.size());
        }
    }

    @Nested
    @DisplayName("Cenários do método atualizar")
    class AtualizarTests {

        @Test
        @DisplayName("Deve lançar NOT_FOUND se o produto a ser atualizado não existir")
        void produtoInexistente() {
            ProdutoRequest request = criarRequest(1L, "Pizza");
            when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> produtoService.atualizar(1L, request));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        }

        @Test
        @DisplayName("Deve atualizar as propriedades do produto com sucesso")
        void atualizarComSucesso() {
            Long produtoId = 10L;
            ProdutoRequest request = criarRequest(2L, "Nome Atualizado");
            Categoria categoriaAntiga = criarCategoria(1L, "Antiga");
            Categoria categoriaNova = criarCategoria(2L, "Nova");
            Produto produtoExistente = criarProduto(produtoId, "Nome Antigo", categoriaAntiga);

            when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));
            when(categoriaRepository.findById(2L)).thenReturn(Optional.of(categoriaNova));
            when(produtoRepository.save(any(Produto.class))).thenAnswer(i -> i.getArgument(0));

            ProdutoResponse resultado = produtoService.atualizar(produtoId, request);

            assertNotNull(resultado);
            verify(produtoRepository, times(1)).save(produtoExistente);
        }
    }

    @Nested
    @DisplayName("Cenários do método deletar")
    class DeletarTests {

        @Test
        @DisplayName("Deve lançar NOT_FOUND se tentar deletar um produto inexistente")
        void deletarInexistente() {
            when(produtoRepository.existsById(1L)).thenReturn(false);

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> produtoService.deletar(1L));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        }

        @Test
        @DisplayName("Deve lançar CONFLICT se o produto possuir dependência com algum item de pedido")
        void deletarComVinculoEmPedido() {
            when(produtoRepository.existsById(1L)).thenReturn(true);
            when(itemPedidoRepository.existsByProdutoId(1L)).thenReturn(true);

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> produtoService.deletar(1L));
            assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
            assertTrue(ex.getReason().contains("vinculado a pedidos existentes"));
        }

        @Test
        @DisplayName("Deve limpar os relacionamentos de ingredientes e remover o produto com sucesso")
        void deletarComSucesso() {
            when(produtoRepository.existsById(1L)).thenReturn(true);
            when(itemPedidoRepository.existsByProdutoId(1L)).thenReturn(false);

            assertDoesNotThrow(() -> produtoService.deletar(1L));

            verify(produtoRepository, times(1)).removerTodosIngredientesDoProduto(1L);
            verify(produtoRepository, times(1)).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("Cenários do método listarPorCategoriaAgrupados")
    class ListarAgrupadosTests {

        @Test
        @DisplayName("Deve agrupar corretamente os produtos retornados pelo nome da Categoria")
        void listarAgrupadosComSucesso() {
            Categoria lanches = criarCategoria(1L, "Lanches");
            Categoria bebidas = criarCategoria(2L, "Bebidas");

            List<Produto> listaCompleta = List.of(
                    criarProduto(10L, "X-Burger", lanches),
                    criarProduto(11L, "X-Salada", lanches),
                    criarProduto(20L, "Suco de Uva", bebidas)
            );

            when(produtoRepository.findAll()).thenReturn(listaCompleta);

            Map<String, List<ProdutoResponse>> resultado = produtoService.listarPorCategoriaAgrupados();

            assertNotNull(resultado);
            assertEquals(2, resultado.keySet().size());
            assertEquals(2, resultado.get("Lanches").size());
            assertEquals(1, resultado.get("Bebidas").size());
        }
    }

    @Nested
    @DisplayName("Cenários de Gerenciamento de Ingredientes")
    class IngredientesTests {

        @Test
        @DisplayName("Deve invocar o repositório para remover um ingrediente específico do produto")
        void removerIngrediente() {
            assertDoesNotThrow(() -> produtoService.removerIngredienteDoProduto(1L, 5L));
            verify(produtoRepository, times(1)).removerIngredienteDoProduto(1L, 5L);
        }

        @Test
        @DisplayName("Deve retornar a lista de ingredientes vinculados a um produto")
        void buscarIngredientes() {
            List<Ingrediente> ingredientes = List.of(new Ingrediente(), new Ingrediente());
            when(ingredienteRepository.findIngredientesByProdutoId(1L)).thenReturn(ingredientes);

            List<Ingrediente> resultado = produtoService.buscarIngredientesPorProduto(1L);

            assertEquals(2, resultado.size());
        }

        @Test
        @DisplayName("Deve lançar NOT_FOUND ao tentar adicionar ingrediente em produto que não existe")
        void adicionarIngredienteProdutoInexistente() {
            when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> produtoService.adicionarIngrediente(1L, 2L));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        }

        @Test
        @DisplayName("Deve lançar NOT_FOUND ao tentar adicionar ingrediente inexistente a um produto")
        void adicionarIngredienteInexistente() {
            Produto produto = criarProduto(1L, "Pizza", criarCategoria(1L, "Comida"));
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
            when(ingredienteRepository.findById(2L)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> produtoService.adicionarIngrediente(1L, 2L));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        }

        @Test
        @DisplayName("Deve lançar CONFLICT se o ingrediente já estiver vinculado ao produto")
        void adicionarIngredienteDuplicado() {
            Produto produto = criarProduto(1L, "Pizza", criarCategoria(1L, "Comida"));
            Ingrediente ing = new Ingrediente();
            ing.setId(2L);
            produto.getIngredientes().add(ing);

            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
            when(ingredienteRepository.findById(2L)).thenReturn(Optional.of(ing));

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> produtoService.adicionarIngrediente(1L, 2L));
            assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        }

        @Test
        @DisplayName("Deve vincular um ingrediente inédito ao produto com sucesso")
        void adicionarIngredienteComSucesso() {
            Produto produto = criarProduto(1L, "Pizza", criarCategoria(1L, "Comida"));
            Ingrediente ing = new Ingrediente();
            ing.setId(2L);

            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
            when(ingredienteRepository.findById(2L)).thenReturn(Optional.of(ing));
            when(produtoRepository.save(produto)).thenReturn(produto);

            Produto resultado = produtoService.adicionarIngrediente(1L, 2L);

            assertTrue(resultado.getIngredientes().contains(ing));
            verify(produtoRepository, times(1)).save(produto);
        }

        @Test
        @DisplayName("Deve lançar NOT_FOUND na atualização em massa se um ou mais IDs de ingredientes não existirem")
        void atualizarIngredientesComListaIncompleta() {
            Produto produto = criarProduto(1L, "Pizza", criarCategoria(1L, "Comida"));
            List<Long> idsSolitados = List.of(10L, 11L);

            Ingrediente ing1 = new Ingrediente();
            ing1.setId(10L);

            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
            when(ingredienteRepository.findAllById(idsSolitados)).thenReturn(List.of(ing1));

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> produtoService.atualizarIngredientes(1L, idsSolitados));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            assertEquals("Um ou mais ingredientes não encontrados", ex.getReason());
        }

        @Test
        @DisplayName("Deve substituir completamente a lista de ingredientes do produto com sucesso")
        void atualizarIngredientesComSucesso() {
            Produto produto = criarProduto(1L, "Pizza", criarCategoria(1L, "Comida"));

            Ingrediente antigo = new Ingrediente();
            antigo.setId(99L);
            produto.getIngredientes().add(antigo);

            List<Long> idsNovos = List.of(10L, 11L);
            Ingrediente n1 = new Ingrediente(); n1.setId(10L);
            Ingrediente n2 = new Ingrediente(); n2.setId(11L);

            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
            when(ingredienteRepository.findAllById(idsNovos)).thenReturn(List.of(n1, n2));
            when(produtoRepository.save(produto)).thenReturn(produto);

            Produto resultado = produtoService.atualizarIngredientes(1L, idsNovos);

            assertEquals(2, resultado.getIngredientes().size());
            assertFalse(resultado.getIngredientes().contains(antigo));
            verify(produtoRepository, times(1)).save(produto);
        }

        @Test
        @DisplayName("atualizarIngredientes: Deve lançar ResponseStatusException quando o produto não for encontrado")
        void atualizarIngredientesProdutoNaoEncontrado() {
            // Arrange
            Long produtoIdInexistente = 99L;
            List<Long> ingredienteIds = List.of(1L, 2L);

            // Força o repositório a retornar um Optional vazio
            when(produtoRepository.findById(produtoIdInexistente)).thenReturn(Optional.empty());

            // Act & Assert
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                produtoService.atualizarIngredientes(produtoIdInexistente, ingredienteIds);
            });

            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            assertEquals("Produto não encontrado", exception.getReason());

            verify(produtoRepository, times(1)).findById(produtoIdInexistente);
            verifyNoInteractions(ingredienteRepository);
            verify(produtoRepository, never()).save(any());
        }
    }
}