package school.sptech.KentoCafe.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import school.sptech.KentoCafe.entity.Funcionario;
import school.sptech.KentoCafe.exception.EntidadeNaoEncontradoException;
import school.sptech.KentoCafe.exception.FuncionarioConflitoException;
import school.sptech.KentoCafe.repository.FuncionarioRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FuncionarioServiceTest {

}