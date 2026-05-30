package school.sptech.KentoCafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sptech.KentoCafe.dto.token.LoginRequestDto;
import school.sptech.KentoCafe.dto.token.LoginUserDto;
import school.sptech.KentoCafe.dto.token.RecoveryJwtTokenDto;
import school.sptech.KentoCafe.entity.Funcionario;
import school.sptech.KentoCafe.security.JwtService;
import school.sptech.KentoCafe.service.FuncionarioService;

import java.util.Map;

@Tag(name = "Autenticação JWT",
        description = "Endpoints de login e geração de token JWT")
@RestController
@RequestMapping("/auth")
public class AuthController {

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
        System.out.println("📧 Email recebido: " + request.getEmail());
        System.out.println("🔑 Senha recebida: " + request.getSenha());

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getSenha()
                    )
            );
        } catch (Exception e) {
            System.out.println("❌ Erro no authenticate: " + e.getClass().getName());
            System.out.println("❌ Mensagem: " + e.getMessage());
            throw e;
        }

        Funcionario funcionario = funcionarioService.buscarPorEmail(request.getEmail());
        String token = jwtService.gerarToken(funcionario);
        return ResponseEntity.ok(Map.of(
                "token", token,
                "gerente", funcionario.getGerente(),
                "nome", funcionario.getNome()
        ));
    }
}