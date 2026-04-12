package school.sptech.KentoCafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import school.sptech.KentoCafe.dto.funcionario.FuncionarioRequest;
import school.sptech.KentoCafe.dto.funcionario.FuncionarioResponse;
import school.sptech.KentoCafe.entity.Funcionario;
import school.sptech.KentoCafe.mapper.FuncionarioMapper;
import school.sptech.KentoCafe.service.FuncionarioService;

@Controller
@RequestMapping("/funcionario")
public class FuncionarioController {
    @Autowired
    private FuncionarioService funcionarioService;


    @PostMapping("/cadastro")
    public ResponseEntity<FuncionarioResponse> criarFuncionario(@RequestBody FuncionarioRequest funcionarioRequest) {
        Funcionario funcionario=funcionarioService.criarFuncionario(funcionarioRequest);
        FuncionarioResponse responseDto= FuncionarioMapper.toResponse(funcionario);
        return ResponseEntity.status(201).body(responseDto);
    }

    @GetMapping("/none")
    public ResponseEntity<String> getAuthenticationNoneTest() {
        return new ResponseEntity<>("Sem autenticação", HttpStatus.OK);
    }


    @GetMapping("/test")
    public ResponseEntity<String> getAuthenticationTest() {
        return new ResponseEntity<>("Autenticado com sucesso", HttpStatus.OK);
    }

    @GetMapping("/test2")
    public ResponseEntity<String> getAuthenticationTestGerente() {
        return new ResponseEntity<>("Autenticado com  gerente", HttpStatus.OK);
    }
}
