package school.sptech.KentoCafe.dto.token;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciais de login")
public class LoginUserDto {
    @Schema(description = "E-mail do funcionário", example = "joao@kentocafe.com")
    private String email;

    @Schema(description = "Senha de acesso", example = "senha123")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
