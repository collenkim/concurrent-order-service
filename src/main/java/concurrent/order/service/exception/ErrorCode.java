package concurrent.order.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    COMPLETED_ORDER_CANCEL(HttpStatus.BAD_REQUEST, "COMPLETED_ORDER_CANCEL", "완료된 주문은 취소할 수 없습니다."),

    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "NOT_FOUND_PRODUCT", "상품을 찾을 수 없습니다."),
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "NOT_FOUND_ORDER", "주문을 찾을 수 없습니다."),

    LOCK_ACQUISITION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "LOCK_ACQUISITION_FAILED", "락 획득에 실패하였습니다."),

    DEFAULT_500(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR",
        "서버에 오류가 발생하였습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
