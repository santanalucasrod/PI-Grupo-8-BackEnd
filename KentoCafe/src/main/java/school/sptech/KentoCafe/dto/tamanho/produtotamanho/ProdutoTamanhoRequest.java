package school.sptech.KentoCafe.dto.tamanho.produtotamanho;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Schema(description = "Vincula um tamanho a um produto, definindo o preço para essa combinação")
public class ProdutoTamanhoRequest {

    @Schema(description = "ID do tamanho", example = "3")
    @NotNull(message = "Tamanho é obrigatório")
    private Long tamanhoId;

    @Schema(description = "Preço do produto nesse tamanho", example = "15.90")
    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    private BigDecimal precoUnidade;

    public Long getTamanhoId() { return tamanhoId; }
    public void setTamanhoId(Long tamanhoId) { this.tamanhoId = tamanhoId; }
    public BigDecimal getPrecoUnidade() { return precoUnidade; }
    public void setPrecoUnidade(BigDecimal precoUnidade) { this.precoUnidade = precoUnidade; }
}
