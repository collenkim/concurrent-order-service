package concurrent.order.service.exception;

public class InvalidOrderValueException extends OrderException{

    public InvalidOrderValueException(String message) {
        super(message);
    }

    public InvalidOrderValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidOrderValueException(Throwable cause) {
        super(cause);
    }

    protected InvalidOrderValueException(String message, Throwable cause,
                                         boolean enableSuppression,
                                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
