package school.sptech.KentoCafe.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import school.sptech.KentoCafe.dto.token.LoginUserDto;
import school.sptech.KentoCafe.dto.token.RecoveryJwtTokenDto;
import school.sptech.KentoCafe.entity.Funcionario;

import school.sptech.KentoCafe.repository.FuncionarioRepository;
import school.sptech.KentoCafe.security.JwtService;

import java.util.List;


@Service
public class FuncionarioService {

    private final FuncionarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public FuncionarioService(FuncionarioRepository repository,
                              PasswordEncoder passwordEncoder,
                              AuthenticationManager authenticationManager,
                              JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public RecoveryJwtTokenDto autenticar(LoginUserDto loginUserDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.getEmail(),
                        loginUserDto.getPassword()
                )
        );

        Funcionario funcionario = (Funcionario) authentication.getPrincipal();

        return new RecoveryJwtTokenDto(jwtService.gerarToken(funcionario));
    }


    public Funcionario criar(Funcionario funcionario) {
        if (repository.findByEmail(funcionario.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Já existe um funcionário com esse email");
        }
        funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));
        return repository.save(funcionario);
    }

    public List<Funcionario> listarTodos() {
        return repository.findAll();
    }

    public Funcionario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Funcionário não encontrado"));
    }

    public Funcionario buscarPorEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Funcionário não encontrado"));
    }

    public Funcionario atualizar(Long id, Funcionario funcionario) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Funcionário não encontrado");
        }
        funcionario.setId(id);
        funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));
        return repository.save(funcionario);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Funcionário não encontrado");
        }
        repository.deleteById(id);
    }
}
