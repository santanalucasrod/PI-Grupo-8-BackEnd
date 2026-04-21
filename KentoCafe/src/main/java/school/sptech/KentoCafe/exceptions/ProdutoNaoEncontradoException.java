package school.sptech.KentoCafe.exceptions;

public class ProdutoNaoEncontradoException extends RuntimeException {
    public ProdutoNaoEncontradoException() {
        super("Produto não encontrado.");
    }
}
