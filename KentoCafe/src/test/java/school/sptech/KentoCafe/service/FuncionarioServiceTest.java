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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.KentoCafe.dto.funcionario.FuncionarioRequest;
import school.sptech.KentoCafe.dto.token.LoginUserDto;
import school.sptech.KentoCafe.dto.token.RecoveryJwtTokenDto;
import school.sptech.KentoCafe.entity.Funcionario;
import school.sptech.KentoCafe.repository.FuncionarioRepository;
import school.sptech.KentoCafe.security.JwtService;

@ExtendWith(MockitoExtension.class)
class FuncionarioServiceTest {

    @Mock private FuncionarioRepository repository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtService jwtService;

    @InjectMocks private FuncionarioService funcionarioService;

    @Nested
    @DisplayName("Cenários do método autenticar")
    class AutenticarTests {

        @Test
        @DisplayName("Deve retornar RecoveryJwtTokenDto com sucesso quando credenciais forem válidas (Cenário 1.1)")
        void autenticarComSucesso() {
            LoginUserDto loginDto = new LoginUserDto("admin@email.com", "senha123");
            Funcionario funcionarioSimulado = new Funcionario();
            funcionarioSimulado.setEmail("admin@email.com");

            // Mocks necessários para a engrenagem do Spring Security funcionar no teste
            Authentication authenticationMock = mock(Authentication.class);
            when(authenticationMock.getPrincipal()).thenReturn(funcionarioSimulado);

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authenticationMock);
            when(jwtService.gerarToken(funcionarioSimulado)).thenReturn("token-jwt-mockado");

            RecoveryJwtTokenDto resultado = funcionarioService.autenticar(loginDto);

            assertNotNull(resultado);
            assertEquals("token-jwt-mockado", resultado.getToken());
            verify(jwtService, times(1)).gerarToken(funcionarioSimulado);
        }

        @Test
        @DisplayName("Deve repassar a exceção quando as credenciais estiverem incorretas (Cenário 1.2)")
        void autenticarComErro() {
            LoginUserDto loginDto = new LoginUserDto("errado@email.com", "senhaIncorreta");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Usuário ou senha inválidos"));

            assertThrows(BadCredentialsException.class, () -> funcionarioService.autenticar(loginDto));
            verifyNoInteractions(jwtService);
        }
    }

    @Nested
    @DisplayName("Cenários do método criar")
    class CriarTests {

        @Test
        @DisplayName("Deve lançar CONFLICT se o email já estiver cadastrado (Cenário 2.1)")
        void criarComEmailDuplicado() {
            Funcionario novo = new Funcionario();
            novo.setEmail("jaexiste@email.com");

            when(repository.findByEmail("jaexiste@email.com")).thenReturn(Optional.of(new Funcionario()));

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> funcionarioService.criar(novo));
            assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
            assertEquals("Já existe um funcionário com esse email", ex.getReason());
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("Deve criptografar a senha e salvar o funcionário com sucesso (Cenário 2.2)")
        void criarComSucesso() {
            Funcionario novo = new Funcionario();
            novo.setEmail("novo@email.com");
            novo.setSenha("12345");

            when(repository.findByEmail("novo@email.com")).thenReturn(Optional.empty());
            when(passwordEncoder.encode("12345")).thenReturn("senhaCripto");
            when(repository.save(novo)).thenReturn(novo);

            Funcionario resultado = funcionarioService.criar(novo);

            assertNotNull(resultado);
            assertEquals("senhaCripto", resultado.getSenha());
            verify(repository, times(1)).save(novo);
        }
    }

    @Nested
    @DisplayName("Cenários de consultas")
    class ConsultasTests {

        @Test
        @DisplayName("Deve retornar todos os funcionários (Cenário 3.1)")
        void listarTodos() {
            when(repository.findAll()).thenReturn(List.of(new Funcionario(), new Funcionario()));
            List<Funcionario> resultado = funcionarioService.listarTodos();
            assertEquals(2, resultado.size());
        }

        @Test
        @DisplayName("Deve buscar por ID com sucesso (Cenário 3.2)")
        void buscarPorIdComSucesso() {
            Funcionario f = new Funcionario();
            when(repository.findById(1L)).thenReturn(Optional.of(f));

            Funcionario resultado = funcionarioService.buscarPorId(1L);
            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Deve lançar NOT_FOUND quando ID não existir (Cenário 3.3)")
        void buscarPorIdInexistente() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> funcionarioService.buscarPorId(99L));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        }

        @Test
        @DisplayName("Deve buscar por email com sucesso (Cenário 3.4)")
        void buscarPorEmailComSucesso() {
            Funcionario f = new Funcionario();
            when(repository.findByEmail("teste@email.com")).thenReturn(Optional.of(f));

            Funcionario resultado = funcionarioService.buscarPorEmail("teste@email.com");
            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Deve lançar NOT_FOUND quando email não existir (Cenário 3.5)")
        void buscarPorEmailInexistente() {
            when(repository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> funcionarioService.buscarPorEmail("naoexiste@email.com"));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        }
    }

    @Nested
    @DisplayName("Cenários do método atualizar")
    class AtualizarTests {

        @Test
        @DisplayName("Deve lançar NOT_FOUND se tentar atualizar funcionário inexistente (Cenário 4.1)")
        void atualizarInexistente() {
            FuncionarioRequest f = new FuncionarioRequest();
            when(repository.existsById(1L)).thenReturn(false);

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> funcionarioService.atualizar(1L, f));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("Deve atualizar ID, criptografar nova senha e salvar com sucesso (Cenário 4.2)")
        void atualizarComSucesso() {
            Long id = 1L;
            Funcionario funcionarioExistente = new Funcionario();
            funcionarioExistente.setId(id);
            funcionarioExistente.setNome("Antigo Nome");
            funcionarioExistente.setEmail("antigo@email.com");

            FuncionarioRequest dto = new FuncionarioRequest();
            dto.setNome("Novo Nome");
            dto.setEmail("novo@email.com");

            when(repository.findById(id)).thenReturn(Optional.of(funcionarioExistente));
            when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
            when(repository.save(any(Funcionario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Funcionario atualizado = funcionarioService.atualizar(id, dto);

            assertNotNull(atualizado);
            assertEquals("Novo Nome", atualizado.getNome());
            assertEquals("novo@email.com", atualizado.getEmail());

            verify(repository).findById(id);
            verify(repository).findByEmail(dto.getEmail());
            verify(repository).save(funcionarioExistente);
        }
    }

    @Nested
    @DisplayName("Cenários do método deletar")
    class DeletarTests {

        @Test
        @DisplayName("Deve lançar NOT_FOUND ao tentar deletar ID inexistente (Cenário 5.1)")
        void deletarInexistente() {
            when(repository.existsById(1L)).thenReturn(false);

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> funcionarioService.deletar(1L));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            verify(repository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Deve deletar o funcionário com sucesso (Cenário 5.2)")
        void deletarComSucesso() {
            when(repository.existsById(1L)).thenReturn(true);

            assertDoesNotThrow(() -> funcionarioService.deletar(1L));
            verify(repository, times(1)).deleteById(1L);
        }
    }
}