package concurrent.order.service.exception;

public class LockAcquisitionFailedException extends OrderException {

    public LockAcquisitionFailedException(String message) {
        super(message);
    }

    public LockAcquisitionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockAcquisitionFailedException(Throwable cause) {
        super(cause);
    }

    protected LockAcquisitionFailedException(String message, Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
