package concurrent.order.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DEFAULT_500(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.DEFAULT_500_CODE,
        ErrorCode.DEFAULT_500_MESSAGE),
    ;

    public static final String DEFAULT_500_CODE = "INTERNAL_SERVER_ERROR";
    public static final String DEFAULT_500_MESSAGE = "서버에 오류가 발생하였습니다.";

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
