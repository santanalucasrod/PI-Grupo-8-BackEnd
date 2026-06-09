package school.sptech.KentoCafe.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
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
import school.sptech.KentoCafe.dto.ingrediente.IngredienteRequest;
import school.sptech.KentoCafe.entity.Ingrediente;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.repository.IngredienteRepository;
import school.sptech.KentoCafe.repository.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
class IngredienteServiceTest {

    @Mock
    private IngredienteRepository ingredienteRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private IngredienteService ingredienteService;

    @Nested
    @DisplayName("Cenários do método buscarTodos")
    class BuscarTodosTests {

        @Test
        @DisplayName("Deve retornar todos os ingredientes (Cenário 1.1)")
        void deveRetornarTodos() {
            List<Ingrediente> lista = List.of(new Ingrediente(), new Ingrediente());
            when(ingredienteRepository.findAll()).thenReturn(lista);

            List<Ingrediente> resultado = ingredienteService.buscarTodos();

            assertEquals(2, resultado.size());
            verify(ingredienteRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Cenários do método buscarPorId")
    class BuscarPorIdTests {

        @Test
        @DisplayName("Deve retornar o ingrediente quando o ID existir (Cenário 2.1)")
        void deveRetornarQuandoIdExistir() {
            Long id = 1L;
            Ingrediente ingrediente = new Ingrediente();
            ingrediente.setId(id);
            ingrediente.setNome("Bacon");

            when(ingredienteRepository.findById(id)).thenReturn(Optional.of(ingrediente));

            Ingrediente resultado = ingredienteService.buscarPorId(id);

            assertNotNull(resultado);
            assertEquals("Bacon", resultado.getNome());
        }

        @Test
        @DisplayName("Deve lançar NOT_FOUND quando o ID não existir (Cenário 2.2)")
        void deveLancarNotFound() {
            Long id = 99L;
            when(ingredienteRepository.findById(id)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
                ingredienteService.buscarPorId(id);
            });

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            assertEquals("Ingrediente não encontrado", ex.getReason());
        }
    }

    @Nested
    @DisplayName("Cenários do método criar")
    class CriarTests {

        @Test
        @DisplayName("Deve salvar e retornar o ingrediente (Cenário 3.1)")
        void deveCriarComSucesso() {
            Ingrediente novo = new Ingrediente();
            novo.setNome("Cheddar");

            when(ingredienteRepository.save(novo)).thenReturn(novo);

            Ingrediente resultado = ingredienteService.criar(novo);

            assertNotNull(resultado);
            assertEquals("Cheddar", resultado.getNome());
            verify(ingredienteRepository, times(1)).save(novo);
        }
    }

    @Nested
    @DisplayName("Cenários do método atualizar")
    class AtualizarTests {

        @Test
        @DisplayName("Deve lançar NOT_FOUND se o ingrediente a ser atualizado não existir (Cenário 4.1)")
        void deveLancarNotFoundAoAtualizar() {
            Long id = 99L;
            IngredienteRequest req = new IngredienteRequest();
            when(ingredienteRepository.existsById(id)).thenReturn(false);

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
                ingredienteService.atualizar(id, req);
            });

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            verify(ingredienteRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve atualizar o ingrediente com sucesso se o ID existir (Cenário 4.2)")
        void deveAtualizarComSucesso() {
            Long id = 1L;
            IngredienteRequest req = new IngredienteRequest();
            req.setNome("Calabresa Defumada");

            when(ingredienteRepository.existsById(id)).thenReturn(true);
            // O save recebe a entidade vinda do mapper e retorna ela mesma
            when(ingredienteRepository.save(any(Ingrediente.class))).thenAnswer(i -> i.getArgument(0));

            Ingrediente resultado = ingredienteService.atualizar(id, req);

            assertNotNull(resultado);
            assertEquals(id, resultado.getId());
            verify(ingredienteRepository, times(1)).save(any(Ingrediente.class));
        }
    }

    @Nested
    @DisplayName("Cenários do método deletar")
    class DeletarTests {

        @Test
        @DisplayName("Deve lançar NOT_FOUND se tentar deletar um ID inexistente (Cenário 5.1)")
        void deveLancarNotFoundAoDeletar() {
            Long id = 99L;
            when(ingredienteRepository.existsById(id)).thenReturn(false);

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
                ingredienteService.deletar(id);
            });

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            verify(ingredienteRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Deve remover vínculos e deletar ingrediente quando o ID existir (Cenário 5.2)")
        void deveDeletarComSucesso() {
            Long id = 1L;
            when(ingredienteRepository.existsById(id)).thenReturn(true);

            assertDoesNotThrow(() -> ingredienteService.deletar(id));

            verify(ingredienteRepository, times(1)).removerIngredienteDeTodosProdutos(id);
            verify(ingredienteRepository, times(1)).deleteById(id);
        }
    }

    @Nested
    @DisplayName("Cenários do método buscarIngredientesPorProduto")
    class BuscarPorProdutoTests {

        @Test
        @DisplayName("Deve buscar ingredientes vinculados a um produto (Cenário 6.1)")
        void deveBuscarIngredientesDoProduto() {
            Long produtoId = 10L;
            List<Ingrediente> lista = List.of(new Ingrediente());
            when(ingredienteRepository.findIngredientesByProdutoId(produtoId)).thenReturn(lista);

            List<Ingrediente> resultado = ingredienteService.buscarIngredientesPorProduto(produtoId);

            assertEquals(1, resultado.size());
            verify(ingredienteRepository, times(1)).findIngredientesByProdutoId(produtoId);
        }
    }

    @Nested
    @DisplayName("Cenários do método existeProdutosComIngrediente")
    class ExisteProdutosComIngredienteTests {

        @Test
        @DisplayName("Deve retornar true se a contagem for diferente de zero (Cenário 7.1)")
        void deveRetornarTrueQuandoExistir() {
            Long ingredienteId = 5L;
            when(produtoRepository.contarProdutosPorIngrediente(ingredienteId)).thenReturn(3);

            Boolean resultado = ingredienteService.existeProdutosComIngrediente(ingredienteId);

            assertTrue(resultado);
        }

        @Test
        @DisplayName("Deve retornar false se a contagem for zero (Cenário 7.2)")
        void deveRetornarFalseQuandoNaoExistir() {
            Long ingredienteId = 5L;
            when(produtoRepository.contarProdutosPorIngrediente(ingredienteId)).thenReturn(0);

            Boolean resultado = ingredienteService.existeProdutosComIngrediente(ingredienteId);

            assertFalse(resultado);
        }
    }

    @Nested
    @DisplayName("Cenários do método buscarProdutosPorIngrediente")
    class BuscarProdutosPorIngredienteTests {

        @Test
        @DisplayName("Deve listar os produtos vinculados a um ingrediente (Cenário 8.1)")
        void deveBuscarProdutos() {
            Long ingredienteId = 5L;
            List<Produto> produtos = List.of(new Produto(), new Produto());
            when(produtoRepository.findByIngredienteId(ingredienteId)).thenReturn(produtos);

            List<Produto> resultado = ingredienteService.buscarProdutosPorIngrediente(ingredienteId);

            assertEquals(2, resultado.size());
            verify(produtoRepository, times(1)).findByIngredienteId(ingredienteId);
        }
    }
}