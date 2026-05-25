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
       return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<InfoAdicionalResponse> editarPorId(@PathVariable Long id,@RequestBody InfoAdicionalRequest request){
        if(request == null){
            throw new IllegalArgumentException();
        }
        InfoAdicional editado = infoAdicionalService.criar(request);
        InfoAdicionalResponse response = new InfoAdicionalResponse();
        response.setDescricao(editado.getDescricao());
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/excluir")
    public ResponseEntity<InfoAdicionalResponse> excluir(@RequestBody InfoAdicionalRequest request){
        if(request == null){
            throw new IllegalArgumentException();
        }
        InfoAdicional excluido = infoAdicionalService.criar(request);
        InfoAdicionalResponse response = new InfoAdicionalResponse();
        response.setDescricao(excluido.getDescricao());
        return ResponseEntity.status(200).body(response);
    }
}
