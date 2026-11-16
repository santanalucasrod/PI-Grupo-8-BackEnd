package school.sptech.KentoCafe.service.s3;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import school.sptech.KentoCafe.entity.S3CleanupOutbox;
import school.sptech.KentoCafe.repository.S3CleanupOutboxRepository;

@Service
public class S3CleanupOutboxService {

    public record TarefaLimpeza(Long id, String chave) {}

    private final S3CleanupOutboxRepository repository;

    public S3CleanupOutboxService(S3CleanupOutboxRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void agendar(String chave) {
        if (chave == null || chave.isBlank()) {
            return;
        }
        repository.save(new S3CleanupOutbox(chave));
    }

    @Transactional
    public List<TarefaLimpeza> reivindicarPendencias() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime limiteBloqueio = agora.minusMinutes(5);

        List<S3CleanupOutbox> tarefas = repository.buscarParaProcessar(
                S3CleanupOutbox.Status.PENDENTE,
                S3CleanupOutbox.Status.PROCESSANDO,
                agora,
                limiteBloqueio,
                PageRequest.of(0, 20));

        for (S3CleanupOutbox tarefa : tarefas) {
            tarefa.marcarProcessando(agora);
        }

        return tarefas.stream()
                .map(tarefa -> new TarefaLimpeza(tarefa.getId(), tarefa.getObjectKey()))
                .toList();
    }

    @Transactional
    public void marcarConcluida(Long id) {
        repository.findById(id).ifPresent(S3CleanupOutbox::marcarConcluido);
    }

    @Transactional
    public void registrarFalha(Long id, String mensagem) {
        repository.findById(id).ifPresent(tarefa ->
                tarefa.registrarFalha(mensagem, LocalDateTime.now()));
    }
}
