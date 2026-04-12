package school.sptech.KentoCafe.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import school.sptech.KentoCafe.entity.Funcionario;
import school.sptech.KentoCafe.repository.FuncionarioRepository;

import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService  implements UserDetailsService {
    //liga seu usuario com o spring security

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Funcionario user = funcionarioRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        return new UserDetailsImpl(user);
    }

}
