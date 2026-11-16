package school.sptech.KentoCafe.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import school.sptech.KentoCafe.dto.produto.ProdutoResponse;
import school.sptech.KentoCafe.service.s3.ProdutoImagemService;

@RestController
@RequestMapping("/produtos")
public class ProdutoImagemController {

    private final ProdutoImagemService produtoImagemService;

    public ProdutoImagemController(ProdutoImagemService produtoImagemService) {
        this.produtoImagemService = produtoImagemService;
    }

    @PostMapping(value = "/{id}/imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoResponse> enviarOuAtualizar(
            @PathVariable Long id,
            @RequestParam("arquivo") MultipartFile arquivo) {
        return ResponseEntity.ok(produtoImagemService.salvar(id, arquivo));
    }

    @GetMapping("/{id}/imagem")
    public ResponseEntity<Void> obterImagem(@PathVariable Long id) {
        String url = produtoImagemService.obterUrlTemporaria(id);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    @DeleteMapping("/{id}/imagem")
    public ResponseEntity<Void> removerImagem(@PathVariable Long id) {
        produtoImagemService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
