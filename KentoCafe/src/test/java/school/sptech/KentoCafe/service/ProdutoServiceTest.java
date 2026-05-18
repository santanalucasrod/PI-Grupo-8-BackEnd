package school.sptech.KentoCafe.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.KentoCafe.dto.produto.ProdutoResponse;
import school.sptech.KentoCafe.entity.Categoria;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.repository.ProdutoRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    @DisplayName("buscarProdutosPorIngredienteId: Deve retornar lista de produtos (Cenário Feliz)")
    void buscarPorIngredienteCenarioFeliz() {
        // Arrange
        Integer ingredienteId = 1;
        Produto p1 = new Produto();
        p1.setId(10);
        p1.setNome("Café Expresso");

        when(produtoRepository.findByIngredienteId(ingredienteId)).thenReturn(List.of(p1));

        // Act
        List<Produto> resultado = produtoService.buscarProdutosPorIngredienteId(ingredienteId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Café Expresso", resultado.getFirst().getNome());
        verify(produtoRepository, times(1)).findByIngredienteId(ingredienteId);
    }

    @Test
    @DisplayName("buscarProdutosPorIngredienteId: Deve retornar lista vazia se o ID não existir ou não tiver produtos vinculados (Cenário Triste)")
    void buscarPorIngredienteListaVazia() {
        // Arrange
        Integer ingredienteId = 99;
        when(produtoRepository.findByIngredienteId(ingredienteId)).thenReturn(Collections.emptyList());

        // Act
        List<Produto> resultado = produtoService.buscarProdutosPorIngredienteId(ingredienteId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(produtoRepository, times(1)).findByIngredienteId(ingredienteId);
    }

    @Test
    @DisplayName("listarPorCategoria: Deve retornar lista de ProdutoResponse (Cenário Feliz)")
    void listarPorCategoriaCenarioFeliz() {
        // Arrange
        Integer categoriaId = 1;

        Categoria categoriaMock = new Categoria();
        categoriaMock.setId(categoriaId);
        categoriaMock.setNome("Bebidas Quentes");

        Produto p = new Produto();
        p.setId(1);
        p.setNome("Cappuccino");
        p.setPrecoUnidade(7.50);
        p.setCategoria(categoriaMock);

        when(produtoRepository.findByCategoriaId(categoriaId)).thenReturn(List.of(p));

        // Act
        List<ProdutoResponse> resultado = produtoService.listarPorCategoria(categoriaId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        verify(produtoRepository, times(1)).findByCategoriaId(categoriaId);
    }

    @Test
    @DisplayName("listarPorCategoria: Deve retornar lista vazia se a categoria não tiver produtos (Cenário Triste)")
    void listarPorCategoriaVazia() {
        // Arrange
        Integer categoriaId = 404;
        when(produtoRepository.findByCategoriaId(categoriaId)).thenReturn(Collections.emptyList());

        // Act
        List<ProdutoResponse> resultado = produtoService.listarPorCategoria(categoriaId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(produtoRepository, times(1)).findByCategoriaId(categoriaId);
    }

    @Test
    @DisplayName("listarPorCategoriaAgrupados: Deve agrupar os produtos corretamente por nome de categoria (Cenário Feliz)")
    void listarAgrupadosCenarioFeliz() {
        // Arrange
        Categoria c1 = new Categoria();
        c1.setNome("Bebidas Quentes");

        Categoria c2 = new Categoria();
        c2.setNome("Doces");

        Produto p1 = new Produto();
        p1.setNome("Espresso");
        p1.setCategoria(c1);

        Produto p2 = new Produto();
        p2.setNome("Cappuccino");
        p2.setCategoria(c1);

        Produto p3 = new Produto();
        p3.setNome("Croissant");
        p3.setCategoria(c2);

        when(produtoRepository.findAll()).thenReturn(List.of(p1, p2, p3));

        // Act
        Map<String, List<ProdutoResponse>> resultado = produtoService.listarPorCategoriaAgrupados();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.containsKey("Bebidas Quentes"));
        assertTrue(resultado.containsKey("Doces"));

        assertEquals(2, resultado.get("Bebidas Quentes").size());
        assertEquals(1, resultado.get("Doces").size());

        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("listarPorCategoriaAgrupados: Deve retornar mapa vazio se não houver produtos na tabela (Cenário Triste)")
    void listarAgrupadosBancoVazio() {
        // Arrange
        when(produtoRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Map<String, List<ProdutoResponse>> resultado = produtoService.listarPorCategoriaAgrupados();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("listarPorCategoriaAgrupados: Deve lançar NullPointerException se algum produto estiver com a categoria ou nome da categoria nulos (Cenário Triste Crítico)")
    void listarAgrupadosComCategoriaNula() {
        // Arrange
        Produto p1 = new Produto();
        p1.setNome("Produto Sem Categoria");
        p1.setCategoria(null);

        when(produtoRepository.findAll()).thenReturn(List.of(p1));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            produtoService.listarPorCategoriaAgrupados();
        });

        verify(produtoRepository, times(1)).findAll();
    }
}