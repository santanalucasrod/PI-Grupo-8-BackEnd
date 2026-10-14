package school.sptech.KentoCafe.storage;

import school.sptech.KentoCafe.dto.imagem.ImagemValidada;

public interface ImagemStorage {
    String armazenar(Long produtoId, ImagemValidada imagem);
    String gerarUrlTemporaria(String chave);
    void excluir(String chave);
}
