package school.sptech.KentoCafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import school.sptech.KentoCafe.dto.funcionario.FuncionarioRequest;
import school.sptech.KentoCafe.dto.funcionario.FuncionarioResponse;
import school.sptech.KentoCafe.entity.Funcionario;
import school.sptech.KentoCafe.mapper.FuncionarioMapper;
import school.sptech.KentoCafe.service.FuncionarioService;

import java.util.List;

@Tag(name = "Funcionários", description = "Gerenciamento de funcionários e autenticação de acesso")
@Controller
@RequestMapping("/funcionario")
public class FuncionarioController {
    @Autowired
    private FuncionarioService funcionarioService;

    @Operation(summary = "Cadastrar funcionário", description = "Cria um novo funcionário no sistema")
    @ApiResponse(responseCode = "201", description = "Funcionário cadastrado com sucesso")
    @PostMapping("/cadastro")
    public ResponseEntity<FuncionarioResponse> criarFuncionario(@RequestBody FuncionarioRequest funcionarioRequest) {
        Funcionario funcionario=FuncionarioMapper.toEntity(funcionarioRequest);

        Funcionario funcionarioCriado=funcionarioService.criarFuncionario(funcionario);

        FuncionarioResponse responseDto= FuncionarioMapper.toResponse(funcionarioCriado);
        return ResponseEntity.status(201).body(responseDto);
    }

    @GetMapping("/none")
    public ResponseEntity<String> getAuthenticationNoneTest() {
        return new ResponseEntity<>("Sem autenticação", HttpStatus.OK);
    }

    @Operation(summary = "Verificar autenticação", description = "Rota protegida para validar token JWT")
    @ApiResponse(responseCode = "200", description = "Token válido")
    @GetMapping("/autenticado")
    public ResponseEntity<String> getAuthenticationTest() {
        return new ResponseEntity<>("Autenticado com sucesso", HttpStatus.OK);
    }

    @Operation(summary = "Verificar autenticação de gerente", description = "Rota exclusiva para gerentes")
    @GetMapping("/gerenteautenticado")
    public ResponseEntity<String> getAuthenticationTestGerente() {
        return new ResponseEntity<>("Autenticado com  gerente", HttpStatus.OK);
    }

    @Operation(summary = "Listar funcionários")
    @GetMapping("crud")
    public ResponseEntity<List<FuncionarioResponse>> listar() {
        //serviço
        List<Funcionario> funcionarios = funcionarioService.listarFuncionario();

        //dto
        List<FuncionarioResponse> response = FuncionarioMapper.toResponseDto(funcionarios);

        //retorno
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar funcionário por ID")
    @GetMapping("crud/{id}")
    public ResponseEntity<FuncionarioResponse> buscarPorId(@PathVariable Integer id) {
        //servico
        Funcionario funcionario = funcionarioService.buscarFuncionario(id);

        //dto
        FuncionarioResponse responseDto= FuncionarioMapper.toResponse(funcionario);

        //retorno
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Atualizar funcionário", description = "Atualiza informações do funcionário")
    @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso")
    @PutMapping("crud/{id}")
    public ResponseEntity<FuncionarioResponse> atualizar(@PathVariable Integer id, @RequestBody @Valid FuncionarioRequest dto) {
        //dto
        Funcionario funcionario = FuncionarioMapper.toEntity(dto);

        //servico
        Funcionario salvo = funcionarioService.atualizarFuncionario(funcionario,id);

        //dto
        FuncionarioResponse responseDto=FuncionarioMapper.toResponse(salvo);

        //retorno
        return ResponseEntity.ok(responseDto);
    }
    @Operation(summary = "Deletar funcionário")
    @ApiResponse(responseCode = "204", description = "Funcionário deletado com sucesso")
    @DeleteMapping("crud/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        funcionarioService.deletarFuncionario(id);
        return ResponseEntity.noContent().build();
    }
}
