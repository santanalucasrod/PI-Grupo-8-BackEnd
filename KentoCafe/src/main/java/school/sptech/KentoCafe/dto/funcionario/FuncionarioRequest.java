package school.sptech.KentoCafe.dto.funcionario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para cadastro ou atualização de funcionário")
public class FuncionarioRequest {
    @Schema(description = "Nome completo do funcionário", example = "João Silva")
    private String nome;

    @Schema(description = "Senha de acesso", example = "senha123")
    private String senha;

    @Schema(description = "E-mail do funcionário", example = "joao@kentocafe.com")
    private String email;

    @Schema(description = "Indica se o funcionário é gerente", example = "false")
    private Boolean gerente;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getGerente() {
        return gerente;
    }

    public void setGerente(Boolean gerente) {
        this.gerente = gerente;
    }
}

