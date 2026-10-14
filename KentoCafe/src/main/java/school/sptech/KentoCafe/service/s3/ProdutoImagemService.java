package school.sptech.KentoCafe.service.s3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import school.sptech.KentoCafe.dto.imagem.ImagemValidada;
import school.sptech.KentoCafe.dto.produto.ProdutoResponse;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.repository.ProdutoRepository;
import school.sptech.KentoCafe.storage.ImagemStorage;

@Service
public class ProdutoImagemService {

    private static final Logger log = LoggerFactory.getLogger(ProdutoImagemService.class);

    private final ProdutoRepository produtoRepository;
    private final ValidadorImagemService validadorImagemService;
    private final ImagemStorage imagemStorage;
    private final ProdutoImagemPersistenciaService persistenciaService;

    public ProdutoImagemService(
            ProdutoRepository produtoRepository,
            ValidadorImagemService validadorImagemService,
            ImagemStorage imagemStorage,
            ProdutoImagemPersistenciaService persistenciaService) {
        this.produtoRepository = produtoRepository;
        this.validadorImagemService = validadorImagemService;
        this.imagemStorage = imagemStorage;
        this.persistenciaService = persistenciaService;
    }

    public ProdutoResponse salvar(Long produtoId, MultipartFile arquivo) {
        if (!produtoRepository.existsById(produtoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Produto não encontrado.");
        }

        ImagemValidada imagem = validadorImagemService.validar(arquivo);
        String chaveNova = imagemStorage.armazenar(produtoId, imagem);

        try {
            return persistenciaService.vincularImagem(produtoId, chaveNova);
        } catch (RuntimeException erroPersistencia) {
            try {
                imagemStorage.excluir(chaveNova);
            } catch (RuntimeException erroCompensacao) {
                erroPersistencia.addSuppressed(erroCompensacao);
                log.error("Não foi possível compensar o upload; será agendada limpeza. Chave: {}",
                        chaveNova, erroCompensacao);
                try {
                    persistenciaService.agendarLimpezaCompensatoria(chaveNova);
                } catch (RuntimeException erroOutbox) {
                    erroPersistencia.addSuppressed(erroOutbox);
                    log.error("Também não foi possível registrar a limpeza compensatória. "
                                    + "Ação manual poderá ser necessária. Chave: {}",
                            chaveNova, erroOutbox);
                }
            }
            throw erroPersistencia;
        }
    }

    public String obterUrlTemporaria(Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produto não encontrado."));

        String chave = produto.getPathFt();
        if (chave == null || chave.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Este produto não possui imagem cadastrada.");
        }

        return imagemStorage.gerarUrlTemporaria(chave);
    }

    public void remover(Long produtoId) {
        persistenciaService.desvincularImagem(produtoId);
        // A exclusão física é processada de forma assíncrona pelo Outbox Worker.
    }
}
