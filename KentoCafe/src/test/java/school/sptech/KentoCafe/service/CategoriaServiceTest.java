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
import school.sptech.KentoCafe.dto.categoria.CategoriaRequest;
import school.sptech.KentoCafe.dto.categoria.CategoriaResponse;
import school.sptech.KentoCafe.entity.Categoria;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.repository.CategoriaRepository;
import school.sptech.KentoCafe.repository.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Nested
    @DisplayName("Cenários do método criar")
    class CriarTests {

        @Test
        @DisplayName("Deve criar uma categoria com sucesso (Cenário 1.1)")
        void deveCriarCategoriaComSucesso() {
            CategoriaRequest request = new CategoriaRequest();
            request.setNome("Bebidas");

            Categoria categoriaSalva = new Categoria();
            categoriaSalva.setId(1L);
            categoriaSalva.setNome("Bebidas");

            when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaSalva);

            CategoriaResponse resultado = categoriaService.criar(request);

            assertNotNull(resultado);
            verify(categoriaRepository, times(1)).save(any(Categoria.class));
        }
    }

    @Nested
    @DisplayName("Cenários do método listarTodos")
    class ListarTodosTests {

        @Test
        @DisplayName("Deve listar todas as categorias buscando os produtos de cada uma delas (Cenário 2.1)")
        void deveListarTodasAsCategorias() {
            // Given
            Categoria cat1 = new Categoria(); cat1.setId(1L); cat1.setNome("Pizzas");
            Categoria cat2 = new Categoria(); cat2.setId(2L); cat2.setNome("Bebidas");

            Produto p1 = new Produto();
            p1.setId(10L);
            p1.setNome("Pizza de Calabresa");
            p1.setCategoria(cat1);
            p1.setPrecoUnidade(java.math.BigDecimal.valueOf(45.00));

            when(categoriaRepository.findAll()).thenReturn(List.of(cat1, cat2));

            when(produtoRepository.findByCategoriaId(1L)).thenReturn(List.of(p1));
            when(produtoRepository.findByCategoriaId(2L)).thenReturn(List.of());

            // When
            List<CategoriaResponse> resultado = categoriaService.listarTodos();

            // Then
            assertEquals(2, resultado.size());
            verify(produtoRepository, times(1)).findByCategoriaId(1L);
            verify(produtoRepository, times(1)).findByCategoriaId(2L);
        }
    }

    @Nested
    @DisplayName("Cenários do método buscarPorId")
    class BuscarPorIdTests {

        @Test
        @DisplayName("Deve retornar a categoria e seus produtos quando o ID existir (Cenário 3.1)")
        void deveBuscarPorIdComSucesso() {
            // Given
            Long id = 1L;
            Categoria categoria = new Categoria();
            categoria.setId(id);
            categoria.setNome("Sobremesas");

            Produto p1 = new Produto();
            p1.setId(20L);
            p1.setNome("Pudim");
            p1.setCategoria(categoria);
            p1.setPrecoUnidade(java.math.BigDecimal.valueOf(12.00));

            when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));
            when(produtoRepository.findByCategoriaId(id)).thenReturn(List.of(p1));

            // When
            CategoriaResponse resultado = categoriaService.buscarPorId(id);

            // Then
            assertNotNull(resultado);
            verify(produtoRepository, times(1)).findByCategoriaId(id);
        }

        @Test
        @DisplayName("Deve lançar NOT_FOUND quando o ID da categoria não existir (Cenário 3.2)")
        void deveLancarNotFoundAoBuscarPorIdInexistente() {
            Long id = 99L;
            when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
                categoriaService.buscarPorId(id);
            });

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            verifyNoInteractions(produtoRepository);
        }
    }

    @Nested
    @DisplayName("Cenários do método atualizar")
    class AtualizarTests {

        @Test
        @DisplayName("Deve lançar NOT_FOUND se tentar atualizar categoria inexistente (Cenário 4.1)")
        void deveLancarNotFoundAoAtualizarInexistente() {
            Long id = 99L;
            CategoriaRequest request = new CategoriaRequest();
            when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
                categoriaService.atualizar(id, request);
            });

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            verify(categoriaRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve atualizar o nome e salvar a categoria com sucesso (Cenário 4.2)")
        void deveAtualizarComSucesso() {
            Long id = 1L;
            CategoriaRequest request = new CategoriaRequest();
            request.setNome("Pizzas Doces");

            Categoria existente = new Categoria();
            existente.setId(id);
            existente.setNome("Pizzas Salgadas");

            when(categoriaRepository.findById(id)).thenReturn(Optional.of(existente));
            when(categoriaRepository.save(existente)).thenReturn(existente);
            when(produtoRepository.findByCategoriaId(id)).thenReturn(List.of());

            CategoriaResponse resultado = categoriaService.atualizar(id, request);

            assertNotNull(resultado);
            verify(categoriaRepository, times(1)).save(existente);
        }
    }

    @Nested
    @DisplayName("Cenários do método deletar")
    class DeletarTests {

        @Test
        @DisplayName("Deve lançar NOT_FOUND se tentar deletar ID inexistente (Cenário 5.1)")
        void deveLancarNotFoundAoDeletar() {
            Long id = 99L;
            when(categoriaRepository.existsById(id)).thenReturn(false);

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
                categoriaService.deletar(id);
            });

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            verify(categoriaRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Deve lançar CONFLICT se a categoria possuir produtos vinculados (Cenário 5.2)")
        void deveLancarConflictSePossuirProdutos() {
            Long id = 1L;
            when(categoriaRepository.existsById(id)).thenReturn(true);

            // Simula lista de produtos NÃO vazia vinculada à categoria
            when(produtoRepository.findByCategoriaId(id)).thenReturn(List.of(new Produto()));

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
                categoriaService.deletar(id);
            });

            assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
            assertTrue(ex.getReason().contains("possui produtos vinculados"));
            verify(categoriaRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Deve deletar a categoria com sucesso se não houver vínculos (Cenário 5.3)")
        void deveDeletarComSucesso() {
            Long id = 1L;
            when(categoriaRepository.existsById(id)).thenReturn(true);
            when(produtoRepository.findByCategoriaId(id)).thenReturn(List.of()); // Lista vazia

            assertDoesNotThrow(() -> categoriaService.deletar(id));

            verify(categoriaRepository, times(1)).deleteById(id);
        }
    }
}