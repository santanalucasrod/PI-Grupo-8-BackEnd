package school.sptech.KentoCafe.dto.pedido.pedido;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import school.sptech.KentoCafe.dto.pedido.item.ItemRequest;

import java.util.List;

@Schema(description = "Dados para criação de um pedido")
public class PedidoRequest {

    @Schema(description = "Nome do cliente", example = "Gabriela")
    @NotBlank(message = "Nome do cliente é obrigatório")
    private String nomeCliente;

    @Schema(description = "ID do funcionário que registrou o pedido", example = "1")
    @NotNull(message = "Funcionário é obrigatório")
    private Long funcionarioId;

    @Schema(description = "Lista de itens do pedido")
    @NotEmpty(message = "O pedido deve ter pelo menos um item")
    private List<ItemRequest> itens;

    public PedidoRequest(String nomeCliente, Long funcionarioId, List<ItemRequest> itens) {
        this.nomeCliente = nomeCliente;
        this.funcionarioId = funcionarioId;
        this.itens = itens;
    }

    public PedidoRequest() {
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public List<ItemRequest> getItens() {
        return itens;
    }

    public void setItens(List<ItemRequest> itens) {
        this.itens = itens;
    }
}
