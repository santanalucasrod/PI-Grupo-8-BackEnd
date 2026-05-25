package school.sptech.KentoCafe.dto.pedido.InfoAdicional;

public class InfoAdicionalRequest {
    private String descricao;

    public InfoAdicionalRequest(){

    }
    public InfoAdicionalRequest( String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
