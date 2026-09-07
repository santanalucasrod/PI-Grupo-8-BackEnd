package school.sptech.KentoCafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sptech.KentoCafe.dto.token.LoginRequestDto;
import school.sptech.KentoCafe.entity.Funcionario;
import school.sptech.KentoCafe.security.JwtService;
import school.sptech.KentoCafe.service.FuncionarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Tag(name = "Autenticação JWT",
        description = "Endpoints de login e geração de token JWT")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final FuncionarioService funcionarioService;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthController(FuncionarioService funcionarioService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.funcionarioService = funcionarioService;
        this.jwtService = jwtService;
        this.authManager = authenticationManager;
    }

    @Operation(summary = "Fazer login",
            description = "Autentica o funcionário e retorna o token JWT para uso nos demais endpoints")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "401", description = "Email ou senha incorretos")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        log.info("Tentativa de login para o usuário: {}", request.getEmail());

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getSenha()
                    )
            );
        } catch (AuthenticationException e) {
            log.warn("Falha de autenticação para o usuário: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        } catch (Exception e) {
            log.error("Erro interno durante o processamento do login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no servidor");
        }

        Funcionario funcionario = funcionarioService.buscarPorEmail(request.getEmail());
        String token = jwtService.gerarToken(funcionario);

        log.info("Login realizado com sucesso para: {}", request.getEmail());

        return ResponseEntity.ok(Map.of(
                "id", funcionario.getId(),
                "token", token,
                "gerente", funcionario.getGerente(),
                "nome", funcionario.getNome()
        ));
    }
}