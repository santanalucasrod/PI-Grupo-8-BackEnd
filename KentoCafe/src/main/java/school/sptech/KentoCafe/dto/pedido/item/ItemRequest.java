package school.sptech.KentoCafe.dto.pedido.item;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Item de um pedido")
public class ItemRequest {
    @Schema(description = "ID do produto", example = "1")
    @NotNull
    @Positive
    private Integer produtoId;

    @Schema(description = "Quantidade do produto", example = "2")
    @Min(value = 1, message = "A quantidade mínima é 1")
    private Integer quantidade;

    public Integer getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Integer produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
