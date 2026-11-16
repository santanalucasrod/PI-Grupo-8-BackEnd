package school.sptech.KentoCafe.storage;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import school.sptech.KentoCafe.dto.imagem.ImagemValidada;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Component
public class S3ImagemStorageAdapter implements ImagemStorage {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucket;

    public S3ImagemStorageAdapter(
            S3Client s3Client,
            S3Presigner s3Presigner,
            @Value("${aws.s3.bucket-bronze}") String bucket) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucket = bucket;
    }

    @Override
    public String armazenar(Long produtoId, ImagemValidada imagem) {
        String chave = "produtos/" + produtoId + "/"
                + UUID.randomUUID() + "." + imagem.extensao();

        PutObjectRequest requisicao = PutObjectRequest.builder()
                .bucket(bucket)
                .key(chave)
                .contentType(imagem.contentType())
                .contentDisposition("inline")
                .build();

        try {
            s3Client.putObject(requisicao, RequestBody.fromBytes(imagem.conteudo()));
            return chave;
        } catch (S3Exception | SdkClientException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Não foi possível armazenar a imagem no S3.", e);
        }
    }

    @Override
    public String gerarUrlTemporaria(String chave) {
        GetObjectRequest requisicao = GetObjectRequest.builder()
                .bucket(bucket)
                .key(chave)
                .build();

        GetObjectPresignRequest requisicaoAssinada = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(requisicao)
                .build();

        try {
            return s3Presigner.presignGetObject(requisicaoAssinada)
                    .url().toExternalForm();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Não foi possível gerar o acesso temporário à imagem.", e);
        }
    }

    @Override
    public void excluir(String chave) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(chave)
                    .build());
        } catch (S3Exception | SdkClientException e) {
            throw new IllegalStateException("Falha ao excluir imagem do S3.", e);
        }
    }
}
