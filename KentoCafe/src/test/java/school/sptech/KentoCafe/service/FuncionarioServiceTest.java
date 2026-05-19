package school.sptech.KentoCafe.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import school.sptech.KentoCafe.entity.Funcionario;
import school.sptech.KentoCafe.exception.EntidadeNaoEncontradoException;
import school.sptech.KentoCafe.exception.FuncionarioConflitoException;
import school.sptech.KentoCafe.repository.FuncionarioRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private FuncionarioService funcionarioService;

    Funcionario funcionarioConstruct(){

        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);
        funcionario.setGerente(false);
        funcionario.setNome("funcionario");
        funcionario.setEmail("funcionario@");
        funcionario.setSenha("senha");

        return funcionario;
    }

    @Nested
    @DisplayName("Criar")
    class CriarFuncionario {

        @Test
        @DisplayName("deve criar funcionário")
        void deveCriarFuncionario() {

            Funcionario funcionario = funcionarioConstruct();

            Mockito.when(repository.findByEmail(funcionario.getEmail()))
                    .thenReturn(Optional.empty());

            Mockito.when(passwordEncoder.encode(funcionario.getSenha()))
                    .thenReturn("senhaCriptografada");

            funcionario.setSenha("senhaCriptografada");

            Mockito.when(repository.save(Mockito.any(Funcionario.class)))
                    .thenReturn(funcionario);

            Funcionario funcionarioRetorno =
                    funcionarioService.criarFuncionario(funcionarioConstruct());

            Assertions.assertEquals(1, funcionarioRetorno.getId());
            Assertions.assertEquals("Funcionario", funcionarioRetorno.getNome());
            Assertions.assertEquals("funcionario@gmail.com", funcionarioRetorno.getEmail());
            Assertions.assertEquals("senhaCriptografada", funcionarioRetorno.getSenha());
        }

        @Test
        @DisplayName("deve lançar exceção ao criar funcionário com email duplicado")
        void deveLancarExcecaoEmailDuplicado() {

            Funcionario funcionario = funcionarioConstruct();

            Mockito.when(repository.findByEmail(funcionario.getEmail()))
                    .thenReturn(Optional.of(funcionario));

            Assertions.assertThrows(
                    FuncionarioConflitoException.class,
                    () -> funcionarioService.criarFuncionario(funcionario)
            );

            Mockito.verify(repository, Mockito.never())
                    .save(Mockito.any());
        }
    }

    @Nested
    @DisplayName("Listar funcionários")
    class ListarFuncionario {

        @Test
        @DisplayName("deve listar funcionários")
        void deveListarFuncionarios() {

            Funcionario funcionario = funcionarioConstruct();

            List<Funcionario> lista = List.of(funcionario);

            Mockito.when(repository.findAll())
                    .thenReturn(lista);

            List<Funcionario> listaRetorno =
                    funcionarioService.listarFuncionario();

            Assertions.assertEquals(1, listaRetorno.size());
            Assertions.assertEquals(lista, listaRetorno);
        }
    }

    @Nested
    @DisplayName("Atualizar funcionário")
    class AtualizarFuncionario {

        @Test
        @DisplayName("deve atualizar funcionário")
        void deveAtualizarFuncionario() {

            Funcionario funcionario = funcionarioConstruct();

            funcionario.setNome("Funcionario2");

            Mockito.when(repository.existsById(1))
                    .thenReturn(true);

            Mockito.when(passwordEncoder.encode(funcionario.getSenha()))
                    .thenReturn("senhaNova");

            funcionario.setSenha("senhaNova");

            Mockito.when(repository.save(Mockito.any(Funcionario.class)))
                    .thenReturn(funcionario);

            Funcionario funcionarioRetorno =
                    funcionarioService.atualizarFuncionario(
                            funcionarioConstruct(),
                            1
                    );

            Assertions.assertEquals(1, funcionarioRetorno.getId());
            Assertions.assertEquals("Funcionario2", funcionarioRetorno.getNome());
            Assertions.assertEquals("funcionario@gmail.com", funcionarioRetorno.getEmail());
            Assertions.assertEquals("senhaNova", funcionarioRetorno.getSenha());
        }

        @Test
        @DisplayName("deve lançar exceção ao atualizar funcionário inexistente")
        void deveLancarExcecaoFuncionarioNaoEncontrado() {

            Funcionario funcionario = funcionarioConstruct();

            Mockito.when(repository.existsById(1))
                    .thenReturn(false);

            Assertions.assertThrows(
                    EntidadeNaoEncontradoException.class,
                    () -> funcionarioService.atualizarFuncionario(funcionario, 1)
            );

            Mockito.verify(repository, Mockito.never())
                    .save(Mockito.any());
        }
    }

    @Nested
    @DisplayName("Deletar funcionário")
    class DeletarFuncionario {

        @Test
        @DisplayName("deve deletar funcionário")
        void deveDeletarFuncionario() {

            Mockito.when(repository.existsById(1))
                    .thenReturn(true);

            Assertions.assertDoesNotThrow(
                    () -> funcionarioService.deletarFuncionario(1)
            );

            Mockito.verify(repository)
                    .deleteById(1);
        }

        @Test
        @DisplayName("deve lançar exceção ao deletar funcionário inexistente")
        void deveLancarExcecaoAoDeletarFuncionarioInexistente() {
            Mockito.when(repository.existsById(1))
                    .thenReturn(false);

            Assertions.assertThrows(
                    EntidadeNaoEncontradoException.class,
                    () -> funcionarioService.deletarFuncionario(1)
            );

            Mockito.verify(repository, Mockito.never())
                    .deleteById(Mockito.any());
        }
    }

    @Nested
    @DisplayName("Buscar funcionário")
    class BuscarFuncionario {

        @Test
        @DisplayName("deve buscar funcionário")
        void deveBuscarFuncionario() {
            Funcionario funcionario = funcionarioConstruct();

            Mockito.when(repository.findById(1))
                    .thenReturn(Optional.of(funcionario));

            Funcionario funcionarioRetorno =
                    funcionarioService.buscarFuncionario(1);

            Assertions.assertEquals(funcionario, funcionarioRetorno);
        }

        @Test
        @DisplayName("deve lançar exceção ao buscar funcionário inexistente")
        void deveLancarExcecaoAoBuscarFuncionarioInexistente() {

            Mockito.when(repository.findById(1))
                    .thenReturn(Optional.empty());

            Assertions.assertThrows(
                    RuntimeException.class,
                    () -> funcionarioService.buscarFuncionario(1)
            );
        }
    }
}