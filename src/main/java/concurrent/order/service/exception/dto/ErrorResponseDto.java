package concurrent.order.service.exception.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class ErrorResponseDto {

    @Setter
    @Getter
    public static class Res{

        private int httpStatus;
        private String code;
        private String message;
        private List<Details> details;

        @Builder
        public Res(int httpStatus, String code, String message){
            this.httpStatus = httpStatus;
            this.code = code;
            this.message = message;
        }

        @Builder
        public Res(int httpStatus, String code, String message, List<Details> details){
            this.httpStatus = httpStatus;
            this.code = code;
            this.message = message;
            this.details = details;
        }
    }

    public static class Details{

        private String source;
        private String type;
        private String message;
    }

}
