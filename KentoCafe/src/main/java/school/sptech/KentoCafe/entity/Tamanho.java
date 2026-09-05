package school.sptech.KentoCafe.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tamanho")
public class Tamanho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String nome;

    @Column(name = "volume_ml", nullable = false)
    private Integer volumeMl;

    public Tamanho() {}
    public Tamanho(Long id, String nome, Integer volumeMl) {
        this.id = id; this.nome = nome; this.volumeMl = volumeMl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Integer getVolumeMl() { return volumeMl; }
    public void setVolumeMl(Integer volumeMl) { this.volumeMl = volumeMl; }
}
