package school.sptech.KentoCafe.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import school.sptech.KentoCafe.entity.Funcionario;
import school.sptech.KentoCafe.repository.FuncionarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final FuncionarioRepository repository;

    public UserDetailsServiceImpl(FuncionarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Funcionario funcionario = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Funcionário não encontrado: " + email));

        System.out.println("✅ Usuário encontrado: " + funcionario.getEmail());
        System.out.println("✅ Senha no banco: " + funcionario.getSenha());

        return funcionario;
    }
}
