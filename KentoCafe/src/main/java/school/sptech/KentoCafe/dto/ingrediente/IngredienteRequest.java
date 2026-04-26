package school.sptech.KentoCafe.dto.ingrediente;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para criação ou atualização de ingrediente")
public class IngredienteRequest {
    @Schema(description = "Nome do ingrediente", example = "Leite integral")
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
