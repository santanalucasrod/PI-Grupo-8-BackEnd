package school.sptech.KentoCafe.dto.tamanho.produtotamanho;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Tamanho disponível para um produto, com preço")
public class ProdutoTamanhoResponse {
    private Long tamanhoId;
    private String nomeTamanho;
    private Integer volumeMl;
    private BigDecimal precoUnidade;

    public ProdutoTamanhoResponse() {}
    public ProdutoTamanhoResponse(Long tamanhoId, String nomeTamanho, Integer volumeMl, BigDecimal precoUnidade) {
        this.tamanhoId = tamanhoId; this.nomeTamanho = nomeTamanho;
        this.volumeMl = volumeMl; this.precoUnidade = precoUnidade;
    }
    public Long getTamanhoId() { return tamanhoId; }
    public void setTamanhoId(Long tamanhoId) { this.tamanhoId = tamanhoId; }
    public String getNomeTamanho() { return nomeTamanho; }
    public void setNomeTamanho(String nomeTamanho) { this.nomeTamanho = nomeTamanho; }
    public Integer getVolumeMl() { return volumeMl; }
    public void setVolumeMl(Integer volumeMl) { this.volumeMl = volumeMl; }
    public BigDecimal getPrecoUnidade() { return precoUnidade; }
    public void setPrecoUnidade(BigDecimal precoUnidade) { this.precoUnidade = precoUnidade; }
}
