package school.sptech.KentoCafe.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.KentoCafe.dto.tamanho.TamanhoRequest;
import school.sptech.KentoCafe.entity.Tamanho;
import school.sptech.KentoCafe.mapper.TamanhoMapper;
import school.sptech.KentoCafe.repository.ProdutoTamanhoRepository;
import school.sptech.KentoCafe.repository.TamanhoRepository;
import java.util.List;

@Service
public class TamanhoService {

    private final TamanhoRepository tamanhoRepository;
    private final ProdutoTamanhoRepository produtoTamanhoRepository;

    public TamanhoService(TamanhoRepository tamanhoRepository,
                          ProdutoTamanhoRepository produtoTamanhoRepository) {
        this.tamanhoRepository = tamanhoRepository;
        this.produtoTamanhoRepository = produtoTamanhoRepository;
    }

    public List<Tamanho> buscarTodos() {
        return tamanhoRepository.findAll();
    }

    public Tamanho buscarPorId(Long id) {
        return tamanhoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tamanho não encontrado"));
    }

    public Tamanho criar(TamanhoRequest dto) {
        return tamanhoRepository.save(TamanhoMapper.toEntity(dto));
    }

    public Tamanho atualizar(Long id, TamanhoRequest dto) {
        Tamanho existente = buscarPorId(id);
        existente.setNome(dto.getNome());
        existente.setVolumeMl(dto.getVolumeMl());
        return tamanhoRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        buscarPorId(id);
        if (produtoTamanhoRepository.existsByTamanhoId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Tamanho está em uso por algum produto e não pode ser removido");
        }
        tamanhoRepository.deleteById(id);
    }
}
