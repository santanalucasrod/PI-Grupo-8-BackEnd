package school.sptech.KentoCafe.dto.tamanho;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para criação ou atualização de um tamanho de copo")
public class TamanhoRequest {

    @Schema(description = "Nome do tamanho", example = "Grande")
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Schema(description = "Volume em mililitros", example = "500")
    @NotNull(message = "Volume é obrigatório")
    @Min(value = 1, message = "Volume deve ser maior que zero")
    private Integer volumeMl;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Integer getVolumeMl() { return volumeMl; }
    public void setVolumeMl(Integer volumeMl) { this.volumeMl = volumeMl; }
}
