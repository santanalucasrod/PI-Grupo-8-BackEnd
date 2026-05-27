package school.sptech.KentoCafe.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.KentoCafe.dto.pedido.InfoAdicional.InfoAdicionalRequest;
import school.sptech.KentoCafe.entity.InfoAdicional;
import school.sptech.KentoCafe.entity.Pedido;
import school.sptech.KentoCafe.repository.InfoAdicionalRepository;
import school.sptech.KentoCafe.repository.PedidoRepository;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;



@ExtendWith(MockitoExtension.class)
class InfoAdicionalServiceTest {

    @Mock
    InfoAdicionalRepository infoAdicionalRepository;

    @Mock
    PedidoRepository pedidoRepository;

    @InjectMocks
    InfoAdicionalService infoAdicionalService;

    @Test
    @DisplayName("Deve criar info adicional com sucesso")
    void deveCriarInfoAdicionalComSucesso() {

        InfoAdicionalRequest request = new InfoAdicionalRequest();
        request.setPedidoId(1);
        request.setDescricao("Sem cebola");
        request.setPreferenciaIndividual("Sem sal");

        Pedido pedido = new Pedido();
        pedido.setId(1);

        InfoAdicional salvo = new InfoAdicional();
        salvo.setId(1);
        salvo.setDescricao("Sem cebola");
        salvo.setPreferenciaIndividual("Sem sal");
        salvo.setPedido(pedido);

        Mockito.when(infoAdicionalRepository.existsByDescricao(request.getDescricao())).thenReturn(true);
        Mockito.when(pedidoRepository.existsById(request.getPedidoId())).thenReturn(true);
        Mockito.when(pedidoRepository.findById(request.getPedidoId())).thenReturn(Optional.of(pedido));
        Mockito.when(infoAdicionalRepository.save(Mockito.any(InfoAdicional.class))).thenReturn(salvo);

        InfoAdicional resultado = infoAdicionalService.criar(request);

        assertNotNull(resultado);
        assertEquals("Sem cebola", resultado.getDescricao());
    }

    @Test
    @DisplayName("Deve lançar exception quando descrição não existir")
    void deveLancarExceptionQuandoDescricaoNaoExistir() {

        InfoAdicionalRequest request = new InfoAdicionalRequest();
        request.setDescricao("Sem molho");

        Mockito.when(infoAdicionalRepository.existsByDescricao(request.getDescricao())).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> infoAdicionalService.criar(request));
    }

    @Test
    @DisplayName("Deve lançar exception quando pedido não existir ao criar")
    void deveLancarExceptionQuandoPedidoNaoExistirAoCriar() {

        InfoAdicionalRequest request = new InfoAdicionalRequest();
        request.setDescricao("Extra queijo");
        request.setPedidoId(1);

        Mockito.when(infoAdicionalRepository.existsByDescricao(request.getDescricao())).thenReturn(true);
        Mockito.when(pedidoRepository.existsById(request.getPedidoId())).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> infoAdicionalService.criar(request));
    }

    @Test
    @DisplayName("Deve chamar save ao criar")
    void deveChamarSaveAoCriar() {

        InfoAdicionalRequest request = new InfoAdicionalRequest();
        request.setPedidoId(1);
        request.setDescricao("Sem tomate");

        Pedido pedido = new Pedido();

        Mockito.when(infoAdicionalRepository.existsByDescricao(request.getDescricao())).thenReturn(true);
        Mockito.when(pedidoRepository.existsById(request.getPedidoId())).thenReturn(true);
        Mockito.when(pedidoRepository.findById(request.getPedidoId())).thenReturn(Optional.of(pedido));

        infoAdicionalService.criar(request);

        Mockito.verify(infoAdicionalRepository, Mockito.times(1))
                .save(Mockito.any(InfoAdicional.class));
    }

    @Test
    @DisplayName("Deve buscar pedido ao criar")
    void deveBuscarPedidoAoCriar() {

        InfoAdicionalRequest request = new InfoAdicionalRequest();
        request.setPedidoId(1);
        request.setDescricao("Sem ketchup");

        Pedido pedido = new Pedido();

        Mockito.when(infoAdicionalRepository.existsByDescricao(request.getDescricao())).thenReturn(true);
        Mockito.when(pedidoRepository.existsById(request.getPedidoId())).thenReturn(true);
        Mockito.when(pedidoRepository.findById(request.getPedidoId())).thenReturn(Optional.of(pedido));

        infoAdicionalService.criar(request);

        Mockito.verify(pedidoRepository, Mockito.times(1))
                .findById(request.getPedidoId());
    }

    @Test
    @DisplayName("Deve editar info adicional com sucesso")
    void deveEditarInfoAdicionalComSucesso() {

        Integer id = 1;

        InfoAdicionalRequest request = new InfoAdicionalRequest();
        request.setPedidoId(2);
        request.setDescricao("Sem alho");
        request.setPreferenciaIndividual("Pouco molho");

        Pedido pedido = new Pedido();
        pedido.setId(2);

        InfoAdicional atualizado = new InfoAdicional();
        atualizado.setId(id);
        atualizado.setDescricao("Sem alho");

        Mockito.when(infoAdicionalRepository.existsById(id)).thenReturn(true);
        Mockito.when(pedidoRepository.existsById(request.getPedidoId())).thenReturn(true);
        Mockito.when(pedidoRepository.findById(request.getPedidoId())).thenReturn(Optional.of(pedido));
        Mockito.when(infoAdicionalRepository.save(Mockito.any(InfoAdicional.class))).thenReturn(atualizado);

        InfoAdicional resultado = infoAdicionalService.editar(id, request);

        assertNotNull(resultado);
        assertEquals("Sem alho", resultado.getDescricao());
    }

    @Test
    @DisplayName("Deve lançar exception quando id não existir ao editar")
    void deveLancarExceptionQuandoIdNaoExistirAoEditar() {

        Integer id = 1;

        InfoAdicionalRequest request = new InfoAdicionalRequest();

        Mockito.when(infoAdicionalRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> infoAdicionalService.editar(id, request));
    }

    @Test
    @DisplayName("Deve lançar exception quando pedido não existir ao editar")
    void deveLancarExceptionQuandoPedidoNaoExistirAoEditar() {

        Integer id = 1;

        InfoAdicionalRequest request = new InfoAdicionalRequest();
        request.setPedidoId(2);

        Mockito.when(infoAdicionalRepository.existsById(id)).thenReturn(true);
        Mockito.when(pedidoRepository.existsById(request.getPedidoId())).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> infoAdicionalService.editar(id, request));
    }

    @Test
    @DisplayName("Deve chamar save ao editar")
    void deveChamarSaveAoEditar() {

        Integer id = 1;

        InfoAdicionalRequest request = new InfoAdicionalRequest();
        request.setPedidoId(1);

        Pedido pedido = new Pedido();

        Mockito.when(infoAdicionalRepository.existsById(id)).thenReturn(true);
        Mockito.when(pedidoRepository.existsById(request.getPedidoId())).thenReturn(true);
        Mockito.when(pedidoRepository.findById(request.getPedidoId())).thenReturn(Optional.of(pedido));

        infoAdicionalService.editar(id, request);

        Mockito.verify(infoAdicionalRepository, Mockito.times(1))
                .save(Mockito.any(InfoAdicional.class));
    }

    @Test
    @DisplayName("Deve buscar pedido ao editar")
    void deveBuscarPedidoAoEditar() {

        Integer id = 1;

        InfoAdicionalRequest request = new InfoAdicionalRequest();
        request.setPedidoId(3);

        Pedido pedido = new Pedido();

        Mockito.when(infoAdicionalRepository.existsById(id)).thenReturn(true);
        Mockito.when(pedidoRepository.existsById(request.getPedidoId())).thenReturn(true);
        Mockito.when(pedidoRepository.findById(request.getPedidoId())).thenReturn(Optional.of(pedido));

        infoAdicionalService.editar(id, request);

        Mockito.verify(pedidoRepository, Mockito.times(1))
                .findById(request.getPedidoId());
    }

    @Test
    @DisplayName("Deve salvar entidade com id correto ao editar")
    void deveSalvarEntidadeComIdCorretoAoEditar() {

        Integer id = 10;

        InfoAdicionalRequest request = new InfoAdicionalRequest();
        request.setPedidoId(1);

        Pedido pedido = new Pedido();

        Mockito.when(infoAdicionalRepository.existsById(id)).thenReturn(true);
        Mockito.when(pedidoRepository.existsById(request.getPedidoId())).thenReturn(true);
        Mockito.when(pedidoRepository.findById(request.getPedidoId())).thenReturn(Optional.of(pedido));

        infoAdicionalService.editar(id, request);

        Mockito.verify(infoAdicionalRepository)
                .save(Mockito.argThat(info -> info.getId().equals(id)));
    }

    @Test
    @DisplayName("Deve excluir com sucesso")
    void deveExcluirComSucesso() {

        Integer id = 1;

        Mockito.when(infoAdicionalRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> infoAdicionalService.excluir(id));
    }

    @Test
    @DisplayName("Deve lançar exception ao excluir id inexistente")
    void deveLancarExceptionAoExcluirIdInexistente() {

        Integer id = 1;

        Mockito.when(infoAdicionalRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> infoAdicionalService.excluir(id));
    }

    @Test
    @DisplayName("Deve chamar deleteById ao excluir")
    void deveChamarDeleteByIdAoExcluir() {

        Integer id = 1;

        Mockito.when(infoAdicionalRepository.existsById(id)).thenReturn(true);

        infoAdicionalService.excluir(id);

        Mockito.verify(infoAdicionalRepository, Mockito.times(1))
                .deleteById(id);
    }

    @Test
    @DisplayName("Não deve chamar deleteById quando lançar exception")
    void naoDeveChamarDeleteByIdQuandoLancarException() {

        Integer id = 1;

        Mockito.when(infoAdicionalRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> infoAdicionalService.excluir(id));

        Mockito.verify(infoAdicionalRepository, Mockito.never())
                .deleteById(Mockito.anyInt());
    }
}

