package school.sptech.KentoCafe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //responsável por controlar as excessões throw new. isso impede de que a excessão seja transformada em 401 pelo middleware

    // conflito
    @ExceptionHandler(FuncionarioConflitoException.class)
    public ResponseEntity<String> handleConflito(FuncionarioConflitoException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(EntidadeNaoEncontradoException.class)
    public ResponseEntity<String> handleNaoEncontrada(EntidadeNaoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

//    @ExceptionHandler(NotAuthorizedException.class)
//    public ResponseEntity<String> handleAutorizado(NotAuthorizedException ex) {
//        return ResponseEntity
//                .status(HttpStatus.UNAUTHORIZED)
//                .body(ex.getMessage());
//    }
}