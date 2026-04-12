package school.sptech.KentoCafe.dto.ingrediente;

public class IngredienteResponse {
    private Integer id;
    private String nome;

    public IngredienteResponse(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public IngredienteResponse() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
