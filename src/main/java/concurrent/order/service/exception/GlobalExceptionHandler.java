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

    @ExceptionHandler(NotFoundOrderException.class)
    public ResponseEntity<ErrorResponseDto.Res> handleNotFoundOrderException(
        NotFoundOrderException e) {

        ErrorResponseDto.Res res = this.getErrorResponseDto(ErrorCode.NOT_FOUND_ORDER.getHttpStatus().value(),
            ErrorCode.NOT_FOUND_ORDER.getCode(), e.getMessage());

        log.error("NotFoundOrderException : {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @ExceptionHandler(NotFoundProductException.class)
    public ResponseEntity<ErrorResponseDto.Res> handleNotFoundProductException(
        NotFoundProductException e) {

        ErrorResponseDto.Res res = this.getErrorResponseDto(ErrorCode.NOT_FOUND_PRODUCT.getHttpStatus().value(),
            ErrorCode.NOT_FOUND_PRODUCT.getCode(), e.getMessage());

        log.error("NotFoundProductException : {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @ExceptionHandler(CompletedOrderCancelException.class)
    public ResponseEntity<ErrorResponseDto.Res> handleCompletedOrderCancelException(
        CompletedOrderCancelException e) {

        ErrorResponseDto.Res res = this.getErrorResponseDto(ErrorCode.COMPLETED_ORDER_CANCEL.getHttpStatus().value(),
            ErrorCode.COMPLETED_ORDER_CANCEL.getCode(), e.getMessage());

        log.error("CompletedOrderCancelException : {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(LockAcquisitionFailedException.class)
    public ResponseEntity<ErrorResponseDto.Res> handleLockAcquisitionFailedException(
        LockAcquisitionFailedException e) {

        ErrorResponseDto.Res res = this.getErrorResponseDto(ErrorCode.LOCK_ACQUISITION_FAILED.getHttpStatus().value(),
            ErrorCode.LOCK_ACQUISITION_FAILED.getCode(), e.getMessage());

        log.error("LockAcquisitionFailedException : {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
    }

    @ExceptionHandler(ProductStockExceededException.class)
    public ResponseEntity<ErrorResponseDto.Res> handleProductStockExceededException(
        ProductStockExceededException e) {

        ErrorResponseDto.Res res = this.getErrorResponseDto(ErrorCode.PRODUCT_STOCK_EXCEEDED.getHttpStatus().value(),
            ErrorCode.PRODUCT_STOCK_EXCEEDED.getCode(), e.getMessage());

        log.error("ProductStockExceededException : {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    private ErrorResponseDto.Res getErrorResponseDto(int httpStatus, String errorCode, String errorMessage) {
        return ErrorResponseDto.Res.builder()
            .httpStatus(httpStatus)
            .code(errorCode)
            .message(errorMessage)
            .build();
    }
}
