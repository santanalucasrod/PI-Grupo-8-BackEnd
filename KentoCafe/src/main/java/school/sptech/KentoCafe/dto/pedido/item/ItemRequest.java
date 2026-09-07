package school.sptech.KentoCafe.dto.pedido.item;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Item de um pedido")
public class ItemRequest {
    @NotNull(message = "Produto é obrigatório")
    private Long produtoId;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade mínima é 1")
    private Integer quantidade;

    @Schema(description = "IDs das personalizações escolhidas", example = "[1, 3]")
    private List<Long> personalizacaoIds;

    @Schema(description = "ID do tamanho escolhido", example = "2")
    private Long tamanhoId;

    public ItemRequest(Long produtoId, Integer quantidade, List<Long> personalizacaoIds, Long tamanhoId) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.personalizacaoIds = personalizacaoIds;
        this.tamanhoId = tamanhoId;
    }

    public ItemRequest(Long produtoId, Integer quantidade, List<Long> personalizacaoIds) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.personalizacaoIds = personalizacaoIds;
    }

    public ItemRequest() {
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public List<Long> getPersonalizacaoIds() {
        return personalizacaoIds;
    }

    public void setPersonalizacaoIds(List<Long> personalizacaoIds) {
        this.personalizacaoIds = personalizacaoIds;
    }

    public Long getTamanhoId() { return tamanhoId; }
    public void setTamanhoId(Long tamanhoId) { this.tamanhoId = tamanhoId; }
}
