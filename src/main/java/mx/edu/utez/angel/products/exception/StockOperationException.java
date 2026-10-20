package mx.edu.utez.angel.products.exception;

public class StockOperationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public StockOperationException(String message) {
        super(message);
    }
}
