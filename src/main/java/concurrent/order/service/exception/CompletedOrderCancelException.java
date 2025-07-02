package concurrent.order.service.exception;

public class CompletedOrderCancelException extends OrderException{

    public CompletedOrderCancelException(String message) {
        super(message);
    }

    public CompletedOrderCancelException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompletedOrderCancelException(Throwable cause) {
        super(cause);
    }

    protected CompletedOrderCancelException(String message, Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
