package school.sptech.KentoCafe.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import school.sptech.KentoCafe.dto.pedido.InfoAdicional.InfoAdicionalRequest;
import school.sptech.KentoCafe.entity.InfoAdicional;
import school.sptech.KentoCafe.entity.Pedido;
import school.sptech.KentoCafe.repository.InfoAdicionalRepository;
import school.sptech.KentoCafe.repository.PedidoRepository;

import java.util.Optional;

@Service
public class InfoAdicionalService {

    private final InfoAdicionalRepository infoAdicionalRepository;
    private final PedidoRepository pedidoRepository;

    public InfoAdicionalService(InfoAdicionalRepository infoAdicionalRepository, PedidoRepository pedidoRepository) {
        this.infoAdicionalRepository = infoAdicionalRepository;
        this.pedidoRepository = pedidoRepository;
    }


    public InfoAdicional criar(InfoAdicionalRequest request){
        if (!infoAdicionalRepository.existsByDescricao(request.getDescricao())){
            throw new EntityNotFoundException();
        }
        if(!pedidoRepository.existsById(request.getPedidoId())){
            throw new EntityNotFoundException();
        }
        Optional<Pedido> pedido = pedidoRepository.findById(request.getPedidoId());

            InfoAdicional entity = new InfoAdicional();
        entity.setDescricao(request.getDescricao());
        entity.setPreferenciaIndividual(request.getPreferenciaIndividual());
        entity.setPedido(pedido.get());
        InfoAdicional criado = infoAdicionalRepository.save(entity);
        return criado;
}

    public InfoAdicional editar(Integer id, InfoAdicionalRequest request){
        if (!infoAdicionalRepository.existsById(id)){
            throw new EntityNotFoundException();
        }
        if(!pedidoRepository.existsById(request.getPedidoId())){
            throw new EntityNotFoundException();
        }
        Optional<Pedido> pedido = pedidoRepository.findById(request.getPedidoId());

        InfoAdicional entity = new InfoAdicional();
        entity.setId(id);
        entity.setDescricao(request.getDescricao());
        entity.setPreferenciaIndividual(request.getPreferenciaIndividual());
        entity.setPedido(pedido.get());
        InfoAdicional atualizado = infoAdicionalRepository.save(entity);
        return atualizado;
    }

    public void excluir(Integer id){
        if (!infoAdicionalRepository.existsById(id)){
            throw new EntityNotFoundException();
        }
       infoAdicionalRepository.deleteById(id);
    }

}
