package school.sptech.KentoCafe.mapper;

import school.sptech.KentoCafe.dto.tamanho.TamanhoRequest;
import school.sptech.KentoCafe.dto.tamanho.TamanhoResponse;
import school.sptech.KentoCafe.entity.Tamanho;
import java.util.List;
import java.util.stream.Collectors;

public class TamanhoMapper {

    public static Tamanho toEntity(TamanhoRequest dto) {
        Tamanho tamanho = new Tamanho();
        tamanho.setNome(dto.getNome());
        tamanho.setVolumeMl(dto.getVolumeMl());
        return tamanho;
    }

    public static TamanhoResponse toResponse(Tamanho tamanho) {
        return new TamanhoResponse(tamanho.getId(), tamanho.getNome(), tamanho.getVolumeMl());
    }

    public static List<TamanhoResponse> toResponseList(List<Tamanho> tamanhos) {
        return tamanhos.stream().map(TamanhoMapper::toResponse).collect(Collectors.toList());
    }
}
