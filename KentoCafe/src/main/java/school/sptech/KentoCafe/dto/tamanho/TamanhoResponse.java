package school.sptech.KentoCafe.dto.tamanho;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de um tamanho de copo")
public class TamanhoResponse {
    private Long id;
    private String nome;
    private Integer volumeMl;

    public TamanhoResponse() {}
    public TamanhoResponse(Long id, String nome, Integer volumeMl) {
        this.id = id; this.nome = nome; this.volumeMl = volumeMl;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Integer getVolumeMl() { return volumeMl; }
    public void setVolumeMl(Integer volumeMl) { this.volumeMl = volumeMl; }
}
