package school.sptech.KentoCafe.dto.funcionario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro ou atualização de funcionário")
public class FuncionarioRequest {

    @NotBlank(message = "O nome não pode estar vazio")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    @Schema(description = "Nome completo do funcionário", example = "João Silva")
    private String nome;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 8, max = 64, message = "A senha deve ter no mínimo 8 caracteres")
    @Schema(description = "Senha de acesso", example = "senha123")
    private String senha;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Schema(description = "E-mail do funcionário", example = "joao@kentocafe.com")
    private String email;

    @NotNull(message = "É necessário informar se o usuário é gerente")
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

