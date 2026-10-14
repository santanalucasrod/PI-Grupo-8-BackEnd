package school.sptech.KentoCafe.service.s3;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import school.sptech.KentoCafe.dto.produto.ProdutoResponse;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.mapper.ProdutoMapper;
import school.sptech.KentoCafe.repository.ProdutoRepository;

@Service
public class ProdutoImagemPersistenciaService {

    private final ProdutoRepository produtoRepository;
    private final S3CleanupOutboxService outboxService;

    public ProdutoImagemPersistenciaService(
            ProdutoRepository produtoRepository,
            S3CleanupOutboxService outboxService) {
        this.produtoRepository = produtoRepository;
        this.outboxService = outboxService;
    }

    @Transactional
    public ProdutoResponse vincularImagem(Long produtoId, String chaveNova) {
        Produto produto = buscarProduto(produtoId);
        String chaveAntiga = produto.getPathFt();

        produto.setPathFt(chaveNova);
        Produto produtoSalvo = produtoRepository.save(produto);

        if (chaveAntiga != null && !chaveAntiga.isBlank()
                && !chaveAntiga.equals(chaveNova)) {
            outboxService.agendar(chaveAntiga);
        }

        return ProdutoMapper.toResponse(produtoSalvo);
    }

    @Transactional
    public void desvincularImagem(Long produtoId) {
        Produto produto = buscarProduto(produtoId);
        String chave = produto.getPathFt();

        if (chave == null || chave.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Este produto não possui imagem cadastrada.");
        }

        produto.setPathFt(null);
        produtoRepository.save(produto);
        outboxService.agendar(chave);
    }

    @Transactional
    public void agendarLimpezaCompensatoria(String chave) {
        outboxService.agendar(chave);
    }

    private Produto buscarProduto(Long produtoId) {
        return produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produto não encontrado."));
    }
}
