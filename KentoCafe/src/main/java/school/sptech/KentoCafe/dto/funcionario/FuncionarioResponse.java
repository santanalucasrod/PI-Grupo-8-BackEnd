package school.sptech.KentoCafe.dto.funcionario;

public class FuncionarioResponse {
    private Integer id;
    private String nome;
    private String senha;
    private String email;
    private Boolean gerente;

    public FuncionarioResponse() {
    }

    public FuncionarioResponse(Integer id, String nome, String senha, String email, Boolean gerente) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.gerente = gerente;
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
