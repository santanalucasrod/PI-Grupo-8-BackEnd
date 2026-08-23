package school.sptech.KentoCafe.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import school.sptech.KentoCafe.entity.Funcionario;
import school.sptech.KentoCafe.repository.FuncionarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final FuncionarioRepository repository;

    public UserDetailsServiceImpl(FuncionarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Funcionario funcionario = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Funcionário não encontrado: " + email));

        log.debug("Dados do usuário {} carregados com sucesso para o contexto de segurança.", email);

        return funcionario;
    }
}
