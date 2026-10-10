package school.sptech.KentoCafe.mapper;

import org.springframework.data.domain.Page;
import school.sptech.KentoCafe.dto.funcionario.FuncionarioRequest;
import school.sptech.KentoCafe.dto.funcionario.FuncionarioResponse;
import school.sptech.KentoCafe.entity.Funcionario;

import java.util.List;

public class FuncionarioMapper {

    // RequestDto -> Entity
    public static Funcionario toEntity(FuncionarioRequest dto) {
        if (dto == null) return null;

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(dto.getNome());
        funcionario.setEmail(dto.getEmail());
        funcionario.setSenha(dto.getSenha());
        funcionario.setGerente(dto.getGerente());


        return funcionario;
    }

    // Entity -> ResponseDto
    public static FuncionarioResponse toResponse(Funcionario funcionario) {
        if (funcionario == null) return null;

        FuncionarioResponse dto = new FuncionarioResponse();
        dto.setId(funcionario.getId());
        dto.setNome(funcionario.getNome());
        dto.setEmail(funcionario.getEmail());
        dto.setGerente(funcionario.getGerente());
        dto.setAtivo(funcionario.getAtivo());

        return dto;
    }

    public static List<FuncionarioResponse> toResponseDto(List<Funcionario> games) {
        return games.stream()
                .map(FuncionarioMapper::toResponse)
                .toList();
    }


    public static Page<FuncionarioResponse> toResponseDto(Page<Funcionario> funcionarios) {
        return funcionarios.map(FuncionarioMapper::toResponse);
    }
}
