package school.sptech.KentoCafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import school.sptech.KentoCafe.dto.token.LoginUserDto;
import school.sptech.KentoCafe.dto.token.RecoveryJwtTokenDto;
import school.sptech.KentoCafe.service.FuncionarioService;

@Tag(name = "Autenticação JWT",
        description = "endpoints de login e geração de token")
@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private FuncionarioService funcionarioService;

    @Operation(description = "Autentica com dados validos sendo apresentados e retorna um token JWT")
    @ApiResponse(responseCode = "200", description = "esse código representa que a operação ocorreu com sucesso" +
            "ou seja o usuário acessou o sistema")
    @PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenDto> authenticateUser(@RequestBody LoginUserDto loginUserDto) {
        RecoveryJwtTokenDto token = funcionarioService.authenticateUser(loginUserDto);
        return ResponseEntity.ok(token);
    }
}