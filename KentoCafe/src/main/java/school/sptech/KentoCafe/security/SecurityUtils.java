package school.sptech.KentoCafe.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import school.sptech.KentoCafe.entity.Funcionario;

//utils, para pegar o id do usuario autenticado
public class SecurityUtils {

    public static Long getUsuarioId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Funcionario funcionario = (Funcionario) authentication.getPrincipal();

        return funcionario.getId();
    }
}