package concurrent.order.service.application.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderCommand(
        @NotBlank(message = "유저 ID는 필수 입니다.")
        String userId,
        @NotEmpty(message = "주문 항목은 하나 이상이어야 합니다.")
        List<CreateOrderItemCommand> items
){}
