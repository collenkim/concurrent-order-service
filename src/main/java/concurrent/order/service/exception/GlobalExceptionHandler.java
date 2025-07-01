package concurrent.order.service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<> handleException(Exception e) {
        // Log the exception (optional)
        System.err.println("An error occurred: " + e.getMessage());

        // Return a generic error message
        return "An unexpected error occurred. Please try again later.";
    }

}
