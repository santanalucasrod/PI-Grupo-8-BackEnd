package school.sptech.KentoCafe.mapper;

import school.sptech.KentoCafe.dto.ingrediente.IngredienteRequest;
import school.sptech.KentoCafe.dto.ingrediente.IngredienteResponse;
import school.sptech.KentoCafe.entity.Ingrediente;

import java.util.List;

public class IngredienteMapper {

    public static Ingrediente toEntity(IngredienteRequest request){
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setNome(request.getNome());
        return ingrediente;
    }

    public static IngredienteResponse toResponse(Ingrediente ingrediente){
        IngredienteResponse res = new IngredienteResponse();
        res.setId(ingrediente.getId());
        res.setNome(ingrediente.getNome());
        return res;
    }

    public static List<IngredienteResponse> toResponseList(List<Ingrediente> ingredientes){
        return ingredientes.stream()
                .map(IngredienteMapper::toResponse)
                .toList();
    }
}
