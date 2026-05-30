package school.sptech.KentoCafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import school.sptech.KentoCafe.entity.Personalizacao;
import school.sptech.KentoCafe.service.PersonalizacaoService;

import java.util.List;

@Tag(name = "Personalizações", description = "Gerenciamento das opções de personalização dos itens")
@RestController
@RequestMapping("/personalizacoes")
public class PersonalizacaoController {

    private final PersonalizacaoService personalizacaoService;

    public PersonalizacaoController(PersonalizacaoService personalizacaoService) {
        this.personalizacaoService = personalizacaoService;
    }

    @Operation(summary = "Listar todas as personalizações")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @ApiResponse(responseCode = "204", description = "Nenhuma personalização cadastrada")
    @GetMapping
    public ResponseEntity<List<Personalizacao>> listarTodas() {
        List<Personalizacao> personalizacoes = personalizacaoService.buscarTodas();
        return personalizacoes.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(personalizacoes);
    }

    @Operation(summary = "Buscar personalização por ID")
    @ApiResponse(responseCode = "200", description = "Personalização encontrada")
    @ApiResponse(responseCode = "404", description = "Personalização não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<Personalizacao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(personalizacaoService.buscarPorId(id));
    }

    @Operation(summary = "Listar personalizações por tipo",
            description = "Filtra por tipo — ex: 'açúcar', 'leite', 'café'")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @ApiResponse(responseCode = "204", description = "Nenhuma personalização encontrada")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Personalizacao>> buscarPorTipo(@PathVariable String tipo) {
        List<Personalizacao> personalizacoes = personalizacaoService.buscarPorTipo(tipo);
        return personalizacoes.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(personalizacoes);
    }

    @Operation(summary = "Listar tipos disponíveis",
            description = "Retorna os tipos cadastrados — ex: ['açúcar', 'leite', 'café']")
    @ApiResponse(responseCode = "200", description = "Tipos retornados com sucesso")
    @GetMapping("/tipos")
    public ResponseEntity<List<String>> buscarTipos() {
        return ResponseEntity.ok(personalizacaoService.buscarTiposDisponiveis());
    }

    @Operation(summary = "Criar personalização",
            description = "Somente gerentes podem cadastrar novas opções")
    @ApiResponse(responseCode = "201", description = "Personalização criada com sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado — somente gerentes")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Personalizacao> criar(@RequestBody @Valid Personalizacao personalizacao) {
        return ResponseEntity.status(201).body(personalizacaoService.criar(personalizacao));
    }

    @Operation(summary = "Atualizar personalização")
    @ApiResponse(responseCode = "200", description = "Personalização atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Personalização não encontrada")
    @ApiResponse(responseCode = "403", description = "Acesso negado — somente gerentes")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Personalizacao> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid Personalizacao request) {
        return ResponseEntity.ok(personalizacaoService.atualizar(id, request));
    }

    @Operation(summary = "Deletar personalização")
    @ApiResponse(responseCode = "204", description = "Personalização deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Personalização não encontrada")
    @ApiResponse(responseCode = "403", description = "Acesso negado — somente gerentes")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        personalizacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
