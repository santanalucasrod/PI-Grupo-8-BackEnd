package school.sptech.KentoCafe.repository;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import school.sptech.KentoCafe.entity.S3CleanupOutbox;

public interface S3CleanupOutboxRepository
        extends JpaRepository<S3CleanupOutbox, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select tarefa from S3CleanupOutbox tarefa
            where (tarefa.status = :pendente and tarefa.proximaTentativaEm <= :agora)
               or (tarefa.status = :processando and tarefa.bloqueadoEm < :limiteBloqueio)
            order by tarefa.id asc
            """)
    List<S3CleanupOutbox> buscarParaProcessar(
            @Param("pendente") S3CleanupOutbox.Status pendente,
            @Param("processando") S3CleanupOutbox.Status processando,
            @Param("agora") LocalDateTime agora,
            @Param("limiteBloqueio") LocalDateTime limiteBloqueio,
            Pageable pageable);
}
