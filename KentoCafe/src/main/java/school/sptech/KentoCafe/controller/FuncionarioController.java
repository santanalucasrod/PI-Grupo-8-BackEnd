package school.sptech.KentoCafe.controller;

import io.swagger.v3.oas.annotations.Operation;
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

@Controller
@RequestMapping("/funcionario")
public class FuncionarioController {
    @Autowired
    private FuncionarioService funcionarioService;

    @Operation(summary = "Cadastrar funcionário",description = "cadastra um novo user/funcionario no sistema")
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

    @Operation(summary = "verificar autenticação", description = "rota protegida para validar token jwt")
    @GetMapping("/autenticado")
    public ResponseEntity<String> getAuthenticationTest() {
        return new ResponseEntity<>("Autenticado com sucesso", HttpStatus.OK);
    }

    @Operation(summary = "verifica autenticação de gerentes", description = "rota exclusiva para autenticação dos gerentes")
    @GetMapping("/gerenteautenticado")
    public ResponseEntity<String> getAuthenticationTestGerente() {
        return new ResponseEntity<>("Autenticado com  gerente", HttpStatus.OK);
    }

    @Operation(summary = "lista funcionários cadastrados", description = "lista todos os users")
    @GetMapping("crud")
    public ResponseEntity<List<FuncionarioResponse>> listar() {
        //serviço
        List<Funcionario> funcionarios = funcionarioService.listarFuncionario();

        //dto
        List<FuncionarioResponse> response = FuncionarioMapper.toResponseDto(funcionarios);

        //retorno
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Busca usuario pelo id", description = "busca usuario especificando pelo id")
    @GetMapping("crud/{id}")
    public ResponseEntity<FuncionarioResponse> buscarPorId(@PathVariable Integer id) {
        //servico
        Funcionario funcionario = funcionarioService.buscarFuncionario(id);

        //dto
        FuncionarioResponse responseDto= FuncionarioMapper.toResponse(funcionario);

        //retorno
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Atualiza usuario", description = "atualiza informações sobre o usuario")
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
    @Operation(summary = "Remove funcionário", description = "remove user funcionario pelo id")
    @DeleteMapping("crud/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        funcionarioService.deletarFuncionario(id);
        return ResponseEntity.noContent().build();
    }
}
