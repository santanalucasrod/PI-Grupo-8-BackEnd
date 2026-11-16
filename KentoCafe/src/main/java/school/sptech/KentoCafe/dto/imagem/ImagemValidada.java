package school.sptech.KentoCafe.dto.imagem;

public record ImagemValidada(
        byte[] conteudo,
        String contentType,
        String extensao
) {
    public ImagemValidada {
        conteudo = conteudo.clone();
    }

    @Override
    public byte[] conteudo() {
        return conteudo.clone();
    }
}
