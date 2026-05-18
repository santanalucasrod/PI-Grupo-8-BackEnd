package school.sptech.KentoCafe.dto.pedido.pedido;

import io.swagger.v3.oas.annotations.media.Schema;
import school.sptech.KentoCafe.dto.pedido.item.ItemRequest;
import school.sptech.KentoCafe.entity.Funcionario;
import school.sptech.KentoCafe.entity.InfoAdicional;
import school.sptech.KentoCafe.entity.Pedido;

import java.util.List;

@Schema(description = "Dados para criação de um pedido")
public class PedidoRequest {

    @Schema(description = "Informações adicionais do pedido")
    private InfoAdicional infoAdicional;

    @Schema(description = "Lista de itens do pedido")
    private List<ItemRequest> itens;

    private Funcionario funcionario;

    @Schema(description = "Nome do cliente do pedido", example = "Gabriela")
    private String nome;

    public InfoAdicional getInfoAdicional() { return infoAdicional; }
    public void setInfoAdicional(InfoAdicional infoAdicional) { this.infoAdicional = infoAdicional; }

    public List<ItemRequest> getItens() { return itens; }
    public void setItens(List<ItemRequest> itens) { this.itens = itens; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
