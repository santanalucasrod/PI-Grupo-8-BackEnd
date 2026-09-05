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
import school.sptech.KentoCafe.entity.Personalizacao;
import school.sptech.KentoCafe.repository.PersonalizacaoRepository;

@ExtendWith(MockitoExtension.class)
class PersonalizacaoServiceTest {

    @Mock
    private PersonalizacaoRepository personalizacaoRepository;

    @InjectMocks
    private PersonalizacaoService personalizacaoService;

    @Nested
    @DisplayName("Cenários do método buscarTodas")
    class BuscarTodasTests {

        @Test
        @DisplayName("Deve retornar a lista de todas as personalizações")
        void deveRetornarTodasAsPersonalizacoes() {
            List<Personalizacao> listaSimulada = List.of(new Personalizacao(), new Personalizacao());
            when(personalizacaoRepository.findAll()).thenReturn(listaSimulada);

            List<Personalizacao> resultado = personalizacaoService.buscarTodas();

            assertEquals(2, resultado.size());
            verify(personalizacaoRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Cenários do método buscarPorId")
    class BuscarPorIdTests {

        @Test
        @DisplayName("Deve retornar a personalização quando o ID existir")
        void deveRetornarPersonalizacaoQuandoIdExistir() {
            Long id = 1L;
            Personalizacao personalizacao = new Personalizacao();
            personalizacao.setId(id);
            personalizacao.setNome("Borda de Catupiry");

            when(personalizacaoRepository.findById(id)).thenReturn(Optional.of(personalizacao));

            Personalizacao resultado = personalizacaoService.buscarPorId(id);

            assertNotNull(resultado);
            assertEquals("Borda de Catupiry", resultado.getNome());
        }

        @Test
        @DisplayName("Deve lançar NOT_FOUND quando o ID não existir")
        void deveLancarNotFoundQuandoIdNaoExistir() {
            Long idInexistente = 99L;
            when(personalizacaoRepository.findById(idInexistente)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
                personalizacaoService.buscarPorId(idInexistente);
            });

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            assertTrue(ex.getReason().contains("Personalização não encontrada"));
        }
    }

    @Nested
    @DisplayName("Cenários do método criar")
    class CriarTests {

        @Test
        @DisplayName("Deve salvar e retornar a nova personalização")
        void deveCriarPersonalizacaoComSucesso() {
            Personalizacao nova = new Personalizacao();
            nova.setNome("Massa Integral");

            Personalizacao salva = new Personalizacao();
            salva.setId(1L);
            salva.setNome("Massa Integral");

            when(personalizacaoRepository.save(nova)).thenReturn(salva);

            Personalizacao resultado = personalizacaoService.criar(nova);

            assertNotNull(resultado.getId());
            assertEquals("Massa Integral", resultado.getNome());
            verify(personalizacaoRepository, times(1)).save(nova);
        }
    }

    @Nested
    @DisplayName("Cenários do método atualizar")
    class AtualizarTests {

        @Test
        @DisplayName("Deve atualizar e salvar os dados quando o ID existir")
        void deveAtualizarComSucesso() {
            Long id = 1L;
            Personalizacao existente = new Personalizacao();
            existente.setId(id);
            existente.setNome("Nome Antigo");

            Personalizacao request = new Personalizacao();
            request.setNome("Nome Novo");

            // Mock do buscarPorId interno e do save
            when(personalizacaoRepository.findById(id)).thenReturn(Optional.of(existente));
            when(personalizacaoRepository.save(any(Personalizacao.class))).thenAnswer(i -> i.getArgument(0));

            Personalizacao resultado = personalizacaoService.atualizar(id, request);

            assertEquals("Nome Novo", resultado.getNome());
            assertEquals(id, resultado.getId());
        }
    }

    @Nested
    @DisplayName("Cenários do método deletar")
    class DeletarTests {

        @Test
        @DisplayName("Deve deletar a personalização com sucesso quando o ID existir")
        void deveDeletarComSucesso() {
            Long id = 1L;
            when(personalizacaoRepository.existsById(id)).thenReturn(true);

            assertDoesNotThrow(() -> personalizacaoService.deletar(id));

            verify(personalizacaoRepository, times(1)).deleteById(id);
        }

        @Test
        @DisplayName("Deve lançar NOT_FOUND ao tentar deletar um ID inexistente")
        void deveLancarNotFoundAoDeletarInexistente() {
            Long idInexistente = 99L;
            when(personalizacaoRepository.existsById(idInexistente)).thenReturn(false);

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
                personalizacaoService.deletar(idInexistente);
            });

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            verify(personalizacaoRepository, never()).deleteById(any());
        }
    }
}