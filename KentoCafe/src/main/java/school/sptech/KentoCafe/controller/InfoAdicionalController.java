package school.sptech.KentoCafe.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.KentoCafe.dto.pedido.InfoAdicional.InfoAdicionalRequest;
import school.sptech.KentoCafe.dto.pedido.InfoAdicional.InfoAdicionalResponse;
import school.sptech.KentoCafe.entity.InfoAdicional;
import school.sptech.KentoCafe.service.InfoAdicionalService;

@Tag(name = "Informações adicionais", description = "Criação e gerenciamento de informações adicionais")
@RestController
@RequestMapping("/infoadicional")
public class InfoAdicionalController {
    private final InfoAdicionalService infoAdicionalService;

    public InfoAdicionalController(InfoAdicionalService infoAdicionalService) {
        this.infoAdicionalService = infoAdicionalService;
    }

    @PostMapping("/criar")
    public ResponseEntity<InfoAdicionalResponse> criar(@RequestBody InfoAdicionalRequest request){
        if(request == null){
            throw new IllegalArgumentException();
        }
       InfoAdicional criado = infoAdicionalService.criar(request);
        InfoAdicionalResponse response = new InfoAdicionalResponse();
        response.setDescricao(criado.getDescricao());
        response.setPreferenciaIndividual(criado.getPreferenciaIndividual());
       return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<InfoAdicionalResponse> editarPorId(@PathVariable Integer id,@RequestBody InfoAdicionalRequest request){
        if(request == null){
            throw new IllegalArgumentException();
        }
        InfoAdicional editado = infoAdicionalService.editar(id,request);
        InfoAdicionalResponse response = new InfoAdicionalResponse();
        response.setDescricao(editado.getDescricao());
        response.setPreferenciaIndividual(editado.getPreferenciaIndividual());
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirPorId(@PathVariable Integer id){
        if(id == null){
            throw new IllegalArgumentException();
        }
        infoAdicionalService.excluir(id);
        return ResponseEntity.status(204).build();
    }
}
