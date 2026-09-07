package school.sptech.KentoCafe.dto.funcionario;

public class FuncionarioResponse {

    private Long id;
    private String nome;
    private String email;
    private Boolean gerente;

    public FuncionarioResponse() {
    }

    public FuncionarioResponse(Long id, String nome, String email, Boolean gerente) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.gerente = gerente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
