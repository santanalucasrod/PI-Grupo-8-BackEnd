package school.sptech.KentoCafe.service;

import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import school.sptech.KentoCafe.auth.JwtService;
import school.sptech.KentoCafe.auth.SecurityConfig;
import school.sptech.KentoCafe.auth.UserDetailsImpl;
import school.sptech.KentoCafe.dto.funcionario.FuncionarioRequest;
import school.sptech.KentoCafe.dto.token.LoginUserDto;
import school.sptech.KentoCafe.dto.token.RecoveryJwtTokenDto;
import school.sptech.KentoCafe.entity.Funcionario;
import school.sptech.KentoCafe.repository.FuncionarioRepository;

import java.util.List;

@Service
public class FuncionarioService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtTokenService;

    @Autowired
    private SecurityConfig securityConfiguration;

    private final FuncionarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioService(FuncionarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }


    // Métod responsável por autenticar um usuário e retornar um token JWT
    public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
        // Cria um objeto de autenticação com o email e a senha do usuário
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword());

        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o objeto UserDetails do usuário autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Gera um token JWT para o usuário autenticado
        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    // Métod responsável por criar um usuário
    public Funcionario criarFuncionario(FuncionarioRequest  funcionarioRequest) {


        // Cria um novo usuário com os dados fornecidos
        Funcionario funcionarioCriado = new Funcionario();
        funcionarioCriado.setEmail(funcionarioRequest.getEmail());
        funcionarioCriado.setSenha(securityConfiguration.passwordEncoder().encode(funcionarioRequest.getSenha()));
        funcionarioCriado.setNome(funcionarioRequest.getNome());
        funcionarioCriado.setGerente(funcionarioRequest.getGerente());

        // Salva o novo usuário no banco de dados
        return repository.save(funcionarioCriado);

    }

}
