package school.sptech.KentoCafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.KentoCafe.dto.tamanho.TamanhoRequest;
import school.sptech.KentoCafe.dto.tamanho.TamanhoResponse;
import school.sptech.KentoCafe.entity.Tamanho;
import school.sptech.KentoCafe.mapper.TamanhoMapper;
import school.sptech.KentoCafe.service.TamanhoService;
import java.util.List;

@Tag(name = "8. Tamanhos", description = "Catálogo de tamanhos de copo para bebidas — cadastro restrito ao gerente")
@RestController
@RequestMapping("/tamanhos")
public class TamanhoController {

    private final TamanhoService tamanhoService;

    public TamanhoController(TamanhoService tamanhoService) {
        this.tamanhoService = tamanhoService;
    }

    @Operation(summary = "Listar tamanhos disponíveis")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<TamanhoResponse>> listarTodos() {
        return ResponseEntity.ok(TamanhoMapper.toResponseList(tamanhoService.buscarTodos()));
    }

    @Operation(summary = "Buscar tamanho por ID")
    @ApiResponse(responseCode = "200", description = "Tamanho encontrado")
    @ApiResponse(responseCode = "404", description = "Tamanho não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<TamanhoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(TamanhoMapper.toResponse(tamanhoService.buscarPorId(id)));
    }

    @Operation(summary = "Criar tamanho", description = "Somente gerentes podem cadastrar novos tamanhos")
    @ApiResponse(responseCode = "201", description = "Tamanho criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "403", description = "Acesso negado — somente gerentes")
    @PostMapping
    public ResponseEntity<TamanhoResponse> criar(@RequestBody @Valid TamanhoRequest dto) {
        Tamanho criado = tamanhoService.criar(dto);
        return ResponseEntity.status(201).body(TamanhoMapper.toResponse(criado));
    }

    @Operation(summary = "Atualizar tamanho")
    @ApiResponse(responseCode = "200", description = "Tamanho atualizado com sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado — somente gerentes")
    @PutMapping("/{id}")
    public ResponseEntity<TamanhoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid TamanhoRequest dto) {
        return ResponseEntity.ok(TamanhoMapper.toResponse(tamanhoService.atualizar(id, dto)));
    }

    @Operation(summary = "Deletar tamanho")
    @ApiResponse(responseCode = "204", description = "Tamanho deletado com sucesso")
    @ApiResponse(responseCode = "409", description = "Tamanho está em uso por algum produto")
    @ApiResponse(responseCode = "403", description = "Acesso negado — somente gerentes")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tamanhoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
