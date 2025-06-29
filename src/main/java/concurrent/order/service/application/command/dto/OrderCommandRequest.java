package concurrent.order.service.application.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderCommandRequest (
        @NotBlank(message = "유저 ID는 필수 입니다.")
        String userId,
        @NotEmpty
        List<OrderItemRequest> items

){}
