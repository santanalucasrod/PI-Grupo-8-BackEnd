package school.sptech.KentoCafe.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.KentoCafe.dto.categoria.CategoriaRequest;
import school.sptech.KentoCafe.dto.categoria.CategoriaResponse;
import school.sptech.KentoCafe.entity.Categoria;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.mapper.CategoriaMapper;
import school.sptech.KentoCafe.repository.CategoriaRepository;
import school.sptech.KentoCafe.repository.ProdutoRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    @DisplayName("criar: Deve criar e retornar uma CategoriaResponse (Cenário Feliz)")
    void criarCenarioFeliz() {
        // Arrange
        CategoriaRequest request = new CategoriaRequest();
        request.setNome("Bebidas Quentes");

        Categoria entidade = new Categoria();
        entidade.setId(1);
        entidade.setNome("Bebidas Quentes");

        CategoriaResponse responseEsperada = new CategoriaResponse();
        responseEsperada.setId(1);
        responseEsperada.setNome("Bebidas Quentes");

        try (MockedStatic<CategoriaMapper> mapperMock = mockStatic(CategoriaMapper.class)) {
            mapperMock.when(() -> CategoriaMapper.toEntity(request)).thenReturn(entidade);
            when(categoriaRepository.save(entidade)).thenReturn(entidade);
            mapperMock.when(() -> CategoriaMapper.toResponse(entidade, List.of())).thenReturn(responseEsperada);

            // Act
            CategoriaResponse resultado = categoriaService.criar(request);

            // Assert
            assertNotNull(resultado);
            assertEquals(1, resultado.getId());
            assertEquals("Bebidas Quentes", resultado.getNome());
            verify(categoriaRepository, times(1)).save(entidade);
        }
    }

    @Test
    @DisplayName("listarTodos: Deve retornar lista de CategoriaResponse com produtos vinculados (Cenário Feliz)")
    void listarTodosCenarioFeliz() {
        // Arrange
        Categoria c1 = new Categoria();
        c1.setId(1);
        c1.setNome("Bebidas Quentes");

        Categoria c2 = new Categoria();
        c2.setId(2);
        c2.setNome("Doces");

        Produto p1 = new Produto();
        p1.setId(10);
        p1.setNome("Café Expresso");
        p1.setCategoria(c1);

        CategoriaResponse resp1 = new CategoriaResponse();
        resp1.setId(1);
        resp1.setNome("Bebidas Quentes");

        CategoriaResponse resp2 = new CategoriaResponse();
        resp2.setId(2);
        resp2.setNome("Doces");

        when(categoriaRepository.findAll()).thenReturn(List.of(c1, c2));
        when(produtoRepository.findByCategoria_Id(1)).thenReturn(List.of(p1));
        when(produtoRepository.findByCategoria_Id(2)).thenReturn(Collections.emptyList());

        try (MockedStatic<CategoriaMapper> mapperMock = mockStatic(CategoriaMapper.class)) {
            mapperMock.when(() -> CategoriaMapper.toResponse(c1, List.of(p1))).thenReturn(resp1);
            mapperMock.when(() -> CategoriaMapper.toResponse(c2, Collections.emptyList())).thenReturn(resp2);

            // Act
            List<CategoriaResponse> resultado = categoriaService.listarTodos();

            // Assert
            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            verify(categoriaRepository, times(1)).findAll();
            verify(produtoRepository, times(1)).findByCategoria_Id(1);
            verify(produtoRepository, times(1)).findByCategoria_Id(2);
        }
    }

    @Test
    @DisplayName("listarTodos: Deve retornar lista vazia quando não há categorias cadastradas (Cenário Triste)")
    void listarTodosVazio() {
        // Arrange
        when(categoriaRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<CategoriaResponse> resultado = categoriaService.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(categoriaRepository, times(1)).findAll();
        verify(produtoRepository, never()).findByCategoria_Id(any());
    }

    @Test
    @DisplayName("buscarPorId: Deve retornar CategoriaResponse quando ID existir (Cenário Feliz)")
    void buscarPorIdCenarioFeliz() {
        // Arrange
        Integer id = 1;

        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNome("Bebidas Quentes");

        Produto p1 = new Produto();
        p1.setId(10);
        p1.setNome("Café Expresso");

        CategoriaResponse responseEsperada = new CategoriaResponse();
        responseEsperada.setId(id);
        responseEsperada.setNome("Bebidas Quentes");

        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));
        when(produtoRepository.findByCategoria_Id(id)).thenReturn(List.of(p1));

        try (MockedStatic<CategoriaMapper> mapperMock = mockStatic(CategoriaMapper.class)) {
            mapperMock.when(() -> CategoriaMapper.toResponse(categoria, List.of(p1))).thenReturn(responseEsperada);

            // Act
            CategoriaResponse resultado = categoriaService.buscarPorId(id);

            // Assert
            assertNotNull(resultado);
            assertEquals(id, resultado.getId());
            assertEquals("Bebidas Quentes", resultado.getNome());
            verify(categoriaRepository, times(1)).findById(id);
            verify(produtoRepository, times(1)).findByCategoria_Id(id);
        }
    }

    @Test
    @DisplayName("buscarPorId: Deve lançar ResponseStatusException 404 quando ID não existir (Cenário Triste)")
    void buscarPorIdNaoEncontrado() {
        // Arrange
        Integer id = 99;
        when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException excecao = assertThrows(ResponseStatusException.class, () -> {
            categoriaService.buscarPorId(id);
        });

        assertEquals(HttpStatus.NOT_FOUND, excecao.getStatusCode());
        assertEquals("Categoria não encontrada", excecao.getReason());
        verify(categoriaRepository, times(1)).findById(id);
        verify(produtoRepository, never()).findByCategoria_Id(any());
    }


    @Test
    @DisplayName("atualizar: Deve atualizar o nome e retornar CategoriaResponse (Cenário Feliz)")
    void atualizarCenarioFeliz() {
        // Arrange
        Integer id = 1;

        CategoriaRequest request = new CategoriaRequest();
        request.setNome("Bebidas Geladas");

        Categoria categoriaExistente = new Categoria();
        categoriaExistente.setId(id);
        categoriaExistente.setNome("Bebidas Quentes");

        Categoria categoriaAtualizada = new Categoria();
        categoriaAtualizada.setId(id);
        categoriaAtualizada.setNome("Bebidas Geladas");

        CategoriaResponse responseEsperada = new CategoriaResponse();
        responseEsperada.setId(id);
        responseEsperada.setNome("Bebidas Geladas");

        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoriaExistente));
        when(produtoRepository.findByCategoria_Id(id)).thenReturn(Collections.emptyList());
        when(categoriaRepository.save(categoriaExistente)).thenReturn(categoriaAtualizada);

        try (MockedStatic<CategoriaMapper> mapperMock = mockStatic(CategoriaMapper.class)) {
            mapperMock.when(() -> CategoriaMapper.toResponse(categoriaAtualizada, Collections.emptyList()))
                    .thenReturn(responseEsperada);

            // Act
            CategoriaResponse resultado = categoriaService.atualizar(id, request);

            // Assert
            assertNotNull(resultado);
            assertEquals("Bebidas Geladas", resultado.getNome());
            verify(categoriaRepository, times(1)).findById(id);
            verify(categoriaRepository, times(1)).save(categoriaExistente);
            verify(produtoRepository, times(1)).findByCategoria_Id(id);
        }
    }

    @Test
    @DisplayName("atualizar: Deve lançar ResponseStatusException 404 quando ID não existir (Cenário Triste)")
    void atualizarNaoEncontrado() {
        // Arrange
        Integer id = 99;
        CategoriaRequest request = new CategoriaRequest();
        request.setNome("Qualquer Nome");

        when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException excecao = assertThrows(ResponseStatusException.class, () -> {
            categoriaService.atualizar(id, request);
        });

        assertEquals(HttpStatus.NOT_FOUND, excecao.getStatusCode());
        assertEquals("Categoria não encontrada", excecao.getReason());
        verify(categoriaRepository, times(1)).findById(id);
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    @DisplayName("deletar: Deve deletar a categoria sem produtos vinculados (Cenário Feliz)")
    void deletarCenarioFeliz() {
        // Arrange
        Integer id = 1;
        when(categoriaRepository.existsById(id)).thenReturn(true);
        when(produtoRepository.findByCategoria_Id(id)).thenReturn(Collections.emptyList());

        // Act
        assertDoesNotThrow(() -> categoriaService.deletar(id));

        // Assert
        verify(categoriaRepository, times(1)).existsById(id);
        verify(produtoRepository, times(1)).findByCategoria_Id(id);
        verify(categoriaRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("deletar: Deve lançar ResponseStatusException 404 quando ID não existir (Cenário Triste)")
    void deletarNaoEncontrado() {
        // Arrange
        Integer id = 99;
        when(categoriaRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        ResponseStatusException excecao = assertThrows(ResponseStatusException.class, () -> {
            categoriaService.deletar(id);
        });

        assertEquals(HttpStatus.NOT_FOUND, excecao.getStatusCode());
        assertEquals("Categoria não encontrada", excecao.getReason());
        verify(categoriaRepository, times(1)).existsById(id);
        verify(categoriaRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deletar: Deve lançar ResponseStatusException 409 quando a categoria possuir produtos vinculados (Cenário Triste)")
    void deletarComProdutosVinculados() {
        // Arrange
        Integer id = 1;

        Produto p1 = new Produto();
        p1.setId(10);
        p1.setNome("Café Expresso");

        when(categoriaRepository.existsById(id)).thenReturn(true);
        when(produtoRepository.findByCategoria_Id(id)).thenReturn(List.of(p1));

        // Act & Assert
        ResponseStatusException excecao = assertThrows(ResponseStatusException.class, () -> {
            categoriaService.deletar(id);
        });

        assertEquals(HttpStatus.CONFLICT, excecao.getStatusCode());
        assertEquals("Categoria não pode ser deletada pois possui produtos vinculados", excecao.getReason());
        verify(categoriaRepository, times(1)).existsById(id);
        verify(produtoRepository, times(1)).findByCategoria_Id(id);
        verify(categoriaRepository, never()).deleteById(any());
    }
}