package school.sptech.KentoCafe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntidadeNaoEncontradoException extends RuntimeException {
    public EntidadeNaoEncontradoException(String message) {
        super(message);
    }
}
