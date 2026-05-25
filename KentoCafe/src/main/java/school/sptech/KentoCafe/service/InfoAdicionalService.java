package school.sptech.KentoCafe.service;

import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import school.sptech.KentoCafe.dto.pedido.InfoAdicional.InfoAdicionalRequest;
import school.sptech.KentoCafe.entity.InfoAdicional;
import school.sptech.KentoCafe.repository.InfoAdicionalRepository;

@Service
public class InfoAdicionalService {

    private final InfoAdicionalRepository infoAdicionalRepository;

    public InfoAdicionalService(InfoAdicionalRepository infoAdicionalRepository) {
        this.infoAdicionalRepository = infoAdicionalRepository;
    }


    public InfoAdicional criar(InfoAdicionalRequest request){
        if (infoAdicionalRepository.existsByDescricao(request.getDescricao())){
            throw new EntityExistsException();
        }
            InfoAdicional entity = new InfoAdicional();
        entity.setDescricao(request.getDescricao());
        InfoAdicional criado = infoAdicionalRepository.save(entity);
        return criado;

}

}
