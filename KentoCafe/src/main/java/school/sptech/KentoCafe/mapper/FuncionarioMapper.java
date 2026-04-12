package school.sptech.KentoCafe.mapper;

import school.sptech.KentoCafe.dto.funcionario.FuncionarioRequest;
import school.sptech.KentoCafe.dto.funcionario.FuncionarioResponse;
import school.sptech.KentoCafe.entity.Funcionario;

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
        dto.setSenha(funcionario.getSenha());
        dto.setGerente(funcionario.getGerente());

        return dto;
    }
}
