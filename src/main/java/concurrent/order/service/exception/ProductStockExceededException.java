package concurrent.order.service.exception;

public class ProductStockExceededException extends OrderException{

    public ProductStockExceededException(String message) {
        super(message);
    }

    public ProductStockExceededException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductStockExceededException(Throwable cause) {
        super(cause);
    }

    protected ProductStockExceededException(String message, Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
