package school.sptech.KentoCafe.auth;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import school.sptech.KentoCafe.entity.Funcionario;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private final Funcionario funcionario; // Classe de usuário que criamos anteriormente

    public UserDetailsImpl(Funcionario funcionario) {
        this.funcionario = funcionario;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    //se é gerente ou não
    public boolean isManager() {
        return funcionario.getGerente();
    }
    @Override
    public String getPassword() {
        return funcionario.getSenha();
    } // Retorna a credencial do usuário que criamos anteriormente

    @Override
    public String getUsername() {
        return funcionario.getEmail();
    } // Retorna o nome de usuário do usuário que criamos anteriormente



    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}