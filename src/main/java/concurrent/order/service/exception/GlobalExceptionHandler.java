package concurrent.order.service.exception;

import concurrent.order.service.exception.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto.Res> handleException(Exception e) {

        ErrorResponseDto.Res res = this.getErrorResponseDto(ErrorCode.DEFAULT_500.getHttpStatus().value(),
            ErrorCode.DEFAULT_500.getCode(), ErrorCode.DEFAULT_500.getMessage());

        log.error("Exception : {}", e.getMessage());

        return ResponseEntity.internalServerError().body(res);
    }

    @ExceptionHandler(NotFoundProductException.class)
    public ResponseEntity<ErrorResponseDto.Res> handleNotFoundProductException(
        NotFoundProductException e) {

        ErrorResponseDto.Res res = this.getErrorResponseDto(ErrorCode.NOT_FOUND_PRODUCT.getHttpStatus().value(),
            ErrorCode.NOT_FOUND_PRODUCT.getCode(), e.getMessage());

        log.error("ProductNotFoundException : {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    private ErrorResponseDto.Res getErrorResponseDto(int httpStatus, String errorCode, String errorMessage) {
        return ErrorResponseDto.Res.builder()
            .httpStatus(httpStatus)
            .code(errorCode)
            .message(errorMessage)
            .build();
    }
}
