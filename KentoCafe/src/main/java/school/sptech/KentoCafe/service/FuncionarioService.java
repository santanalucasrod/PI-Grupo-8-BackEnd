package school.sptech.KentoCafe.service;

import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import school.sptech.KentoCafe.exception.EntidadeNaoEncontradoException;
import school.sptech.KentoCafe.exception.FuncionarioConflitoException;
import school.sptech.KentoCafe.repository.FuncionarioRepository;

import java.util.List;
import java.util.Optional;

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
    public Funcionario criarFuncionario(Funcionario funcionario) {
        Optional<Funcionario> existente = repository.findByEmail(funcionario.getEmail());

        if (existente.isPresent()) {
            throw new FuncionarioConflitoException("Usuário já existe com esse email.");
        }

        funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));

        // Salva o novo usuário no banco de dados
        return repository.save(funcionario);
    }

    public List<Funcionario> listarFuncionario(){
        return repository.findAll();
    }

    public Funcionario atualizarFuncionario(Funcionario funcionario, Integer id){

        boolean existente = repository.existsById(id);

        if (!existente) {
            throw new EntidadeNaoEncontradoException("Usuário não existe");
        }

        funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));
        funcionario.setId(id);

        // Salva o novo usuário no banco de dados
        return repository.save(funcionario);
    }

    public void deletarFuncionario(Integer id){
        if (!repository.existsById(id)){
            throw new RuntimeException("sem id");
        }
        repository.deleteById(id);
    }

    public Funcionario buscarFuncionario(Integer id){
        Optional<Funcionario> funcionarioOptional= repository.findById(id);

        if (funcionarioOptional.isEmpty()){
            throw new RuntimeException("entidade não existe");
        }
        return funcionarioOptional.get();
    }

}
