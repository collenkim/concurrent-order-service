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

    private ErrorResponseDto.Res getErrorResponseDto(int httpStatus, String errorCode, String errorMessage) {
        return ErrorResponseDto.Res.builder()
            .httpStatus(httpStatus)
            .code(errorCode)
            .message(errorMessage)
            .build();
    }
}
