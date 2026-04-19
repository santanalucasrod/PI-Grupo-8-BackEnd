package school.sptech.KentoCafe.controller;

import jakarta.validation.Valid;
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


    @GetMapping("/autenticado")
    public ResponseEntity<String> getAuthenticationTest() {
        return new ResponseEntity<>("Autenticado com sucesso", HttpStatus.OK);
    }

    @GetMapping("/gerenteautenticado")
    public ResponseEntity<String> getAuthenticationTestGerente() {
        return new ResponseEntity<>("Autenticado com  gerente", HttpStatus.OK);
    }


    @GetMapping("crud")
    public ResponseEntity<List<FuncionarioResponse>> listar() {
        //serviço
        List<Funcionario> funcionarios = funcionarioService.listarFuncionario();

        //dto
        List<FuncionarioResponse> response = FuncionarioMapper.toResponseDto(funcionarios);

        //retorno
        return ResponseEntity.ok(response);
    }

    @GetMapping("crud/{id}")
    public ResponseEntity<FuncionarioResponse> buscarPorId(@PathVariable Integer id) {
        //servico
        Funcionario funcionario = funcionarioService.buscarFuncionario(id);

        //dto
        FuncionarioResponse responseDto= FuncionarioMapper.toResponse(funcionario);

        //retorno
        return ResponseEntity.ok(responseDto);
    }


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

    @DeleteMapping("crud/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        funcionarioService.deletarFuncionario(id);
        return ResponseEntity.noContent().build();
    }
}
