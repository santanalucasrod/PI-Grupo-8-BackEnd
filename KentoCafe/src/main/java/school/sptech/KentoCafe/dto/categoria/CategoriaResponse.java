package school.sptech.KentoCafe.dto.categoria;

import school.sptech.KentoCafe.dto.produto.ProdutoResponse;
import java.util.List;

public class CategoriaResponse {

    private Integer id;
    private String nome;
    private List<ProdutoResponse> produtos;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public List<ProdutoResponse> getProdutos() { return produtos; }
    public void setProdutos(List<ProdutoResponse> produtos) { this.produtos = produtos; }
}