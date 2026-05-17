package school.sptech.KentoCafe.dto.pedido.pedido;

import io.swagger.v3.oas.annotations.media.Schema;
import school.sptech.KentoCafe.dto.pedido.item.ItemRequest;
import school.sptech.KentoCafe.entity.Funcionario;
import school.sptech.KentoCafe.entity.InfoAdicional;
import school.sptech.KentoCafe.entity.Pedido;

import java.util.List;

@Schema(description = "Dados para criação de um pedido")
public class PedidoRequest {
    @Schema(description = "Informações ou observações adicionais do pedido", example = "Sem açúcar")
    private InfoAdicional infoAdicional;

    @Schema(description = "Lista de itens do pedido")
    private List<ItemRequest> itens;
    private Funcionario funcionario;

//    public static class InfoAdicional {
//        private Integer id;
//        private List<String> descricao;
//        private Pedido pedido;
//
//        public InfoAdicional(){
//
//        }
//
//        public InfoAdicional(Integer id, List<String> descricao, Pedido pedido) {
//            this.id = id;
//            this.descricao = descricao;
//            this.pedido = pedido;
//        }
//
//        public Integer getId() {
//            return id;
//        }
//
//        public void setId(Integer id) {
//            this.id = id;
//        }
//
//        public List<String> getDescricao() {
//            return descricao;
//        }
//
//        public void setDescricao(List<String> descricao) {
//            this.descricao = descricao;
//        }
//
//        public Pedido getPedido() {
//            return pedido;
//        }
//
//        public void setPedido(Pedido pedido) {
//            this.pedido = pedido;
//        }
//    }
//
//    public InfoAdicional getInfoAdicional() {
//        return infoAdicional;
//    }
//
//    public void setInfoAdicional(InfoAdicional infoAdicional) {
//        this.infoAdicional = infoAdicional;
//    }
//
//    public List<ItemRequest> getItens() {
//        return itens;
//    }
//
//    public void setItens(List<ItemRequest> itens) {
//        this.itens = itens;
//    }


    public InfoAdicional getInfoAdicional() {
        return infoAdicional;
    }

    public void setInfoAdicional(InfoAdicional infoAdicional) {
        this.infoAdicional = infoAdicional;
    }

    public List<ItemRequest> getItens() {
        return itens;
    }

    public void setItens(List<ItemRequest> itens) {
        this.itens = itens;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
}
