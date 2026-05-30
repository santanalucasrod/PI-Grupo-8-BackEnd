package school.sptech.KentoCafe.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.KentoCafe.dto.pedido.item.ItemRequest;
import school.sptech.KentoCafe.dto.pedido.pedido.PedidoRequest;
import school.sptech.KentoCafe.dto.pedido.pedido.PedidoResponse;
import school.sptech.KentoCafe.entity.Funcionario;
import school.sptech.KentoCafe.entity.Pedido;
import school.sptech.KentoCafe.entity.Produto;
import school.sptech.KentoCafe.exception.CarrinhoVazioException;
import school.sptech.KentoCafe.exception.EntidadeNaoEncontradoException;
import school.sptech.KentoCafe.exception.ProdutoNaoEncontradoException;
import school.sptech.KentoCafe.repository.FuncionarioRepository;
import school.sptech.KentoCafe.repository.PedidoRepository;
import school.sptech.KentoCafe.repository.ProdutoRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

}