package school.sptech.KentoCafe.dto.ingrediente;

public class IngredienteRequest {
    private String nome;

    public IngredienteRequest(String nome) {
        this.nome = nome;
    }

    public IngredienteRequest() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
