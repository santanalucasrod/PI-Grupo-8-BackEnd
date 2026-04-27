package school.sptech.KentoCafe.exception;

public class CarrinhoVazioException extends RuntimeException {
    public CarrinhoVazioException() {
        super("Não é possível finalizar um pedido sem itens.");
    }
}
