package school.sptech.KentoCafe.dto.token;

public class RecoveryJwtTokenDto {
    private String token;

    public RecoveryJwtTokenDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
