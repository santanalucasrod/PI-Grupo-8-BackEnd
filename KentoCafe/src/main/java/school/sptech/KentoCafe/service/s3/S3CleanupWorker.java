package school.sptech.KentoCafe.service.s3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import school.sptech.KentoCafe.storage.ImagemStorage;

@Component
public class S3CleanupWorker {

    private static final Logger log = LoggerFactory.getLogger(S3CleanupWorker.class);

    private final S3CleanupOutboxService outboxService;
    private final ImagemStorage imagemStorage;

    public S3CleanupWorker(
            S3CleanupOutboxService outboxService,
            ImagemStorage imagemStorage) {
        this.outboxService = outboxService;
        this.imagemStorage = imagemStorage;
    }

    @Scheduled(fixedDelayString = "${app.imagens.cleanup-delay-ms:30000}")
    public void processarPendencias() {
        for (S3CleanupOutboxService.TarefaLimpeza tarefa
                : outboxService.reivindicarPendencias()) {
            try {
                imagemStorage.excluir(tarefa.chave());
                outboxService.marcarConcluida(tarefa.id());
            } catch (RuntimeException erro) {
                log.error("Falha ao limpar objeto S3. ID da tarefa: {}, chave: {}",
                        tarefa.id(), tarefa.chave(), erro);
                try {
                    outboxService.registrarFalha(tarefa.id(), erro.getMessage());
                } catch (RuntimeException erroBanco) {
                    log.error("Não foi possível registrar a falha da tarefa {}.",
                            tarefa.id(), erroBanco);
                }
            }
        }
    }
}
