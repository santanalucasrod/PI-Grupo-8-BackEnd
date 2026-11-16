package school.sptech.KentoCafe.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(
        name = "s3_cleanup_outbox",
        indexes = {
                @Index(name = "idx_s3_cleanup_due", columnList = "status,next_attempt_at"),
                @Index(name = "idx_s3_cleanup_lock", columnList = "status,locked_at")
        }
)
public class S3CleanupOutbox {

    public enum Status {
        PENDENTE,
        PROCESSANDO,
        CONCLUIDO,
        FALHA_FINAL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "object_key", nullable = false, length = 1024)
    private String objectKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.PENDENTE;

    @Column(nullable = false)
    private int tentativas = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "next_attempt_at", nullable = false)
    private LocalDateTime proximaTentativaEm = LocalDateTime.now();

    @Column(name = "locked_at")
    private LocalDateTime bloqueadoEm;

    @Column(name = "last_error", length = 1000)
    private String ultimoErro;

    protected S3CleanupOutbox() {
    }

    public S3CleanupOutbox(String objectKey) {
        this.objectKey = objectKey;
        this.status = Status.PENDENTE;
        this.tentativas = 0;
        this.criadoEm = LocalDateTime.now();
        this.proximaTentativaEm = this.criadoEm;
    }

    public Long getId() { return id; }
    public String getObjectKey() { return objectKey; }
    public Status getStatus() { return status; }
    public int getTentativas() { return tentativas; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getProximaTentativaEm() { return proximaTentativaEm; }
    public LocalDateTime getBloqueadoEm() { return bloqueadoEm; }
    public String getUltimoErro() { return ultimoErro; }

    public void marcarProcessando(LocalDateTime agora) {
        this.status = Status.PROCESSANDO;
        this.bloqueadoEm = agora;
    }

    public void marcarConcluido() {
        this.status = Status.CONCLUIDO;
        this.bloqueadoEm = null;
        this.ultimoErro = null;
    }

    public void registrarFalha(String mensagem, LocalDateTime agora) {
        this.tentativas++;
        this.ultimoErro = mensagem == null ? "Erro não informado" : mensagem;
        if (this.ultimoErro.length() > 1000) {
            this.ultimoErro = this.ultimoErro.substring(0, 1000);
        }
        this.bloqueadoEm = null;

        if (this.tentativas >= 10) {
            this.status = Status.FALHA_FINAL;
            this.proximaTentativaEm = agora;
            return;
        }

        this.status = Status.PENDENTE;
        long multiplicador = 1L << Math.min(this.tentativas, 7);
        long esperaSegundos = Math.min(3600L, 30L * multiplicador);
        this.proximaTentativaEm = agora.plusSeconds(esperaSegundos);
    }
}
