package concurrent.order.service.exception;

public class NotFoundOrderException extends OrderException{

    public NotFoundOrderException(String message) {
        super(message);
    }

    public NotFoundOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundOrderException(Throwable cause) {
        super(cause);
    }

    protected NotFoundOrderException(String message, Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
