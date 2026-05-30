package school.sptech.KentoCafe.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.KentoCafe.entity.Personalizacao;
import school.sptech.KentoCafe.repository.PersonalizacaoRepository;

import java.util.List;

@Service
public class PersonalizacaoService {

    private final PersonalizacaoRepository personalizacaoRepository;

    public PersonalizacaoService(PersonalizacaoRepository personalizacaoRepository) {
        this.personalizacaoRepository = personalizacaoRepository;
    }

    public List<Personalizacao> buscarTodas() {
        return personalizacaoRepository.findAll();
    }

    public Personalizacao buscarPorId(Long id) {
        return personalizacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Personalização não encontrada"));
    }

    public List<Personalizacao> buscarPorTipo(String tipo) {
        return personalizacaoRepository.findByTipo(tipo);
    }

    public List<String> buscarTiposDisponiveis() {
        return personalizacaoRepository.findTiposDisponiveis();
    }

    public Personalizacao criar(Personalizacao personalizacao) {
        return personalizacaoRepository.save(personalizacao);
    }

    public Personalizacao atualizar(Long id, Personalizacao request) {
        Personalizacao existente = buscarPorId(id);
        existente.setNome(request.getNome());
        existente.setTipo(request.getTipo());
        return personalizacaoRepository.save(existente);
    }

    public void deletar(Long id) {
        if (!personalizacaoRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Personalização não encontrada");
        }
        personalizacaoRepository.deleteById(id);
    }
}
