package concurrent.order.service.application.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateOrderItemCommandDto(
        @NotBlank(message = "상품 ID는 필수입니다.")
        String productId,

        @Positive(message = "수량은 1 이상이어야 합니다.")
        Integer quantity,

        @Positive(message = "상품 가격은 0보다 커야 합니다.")
        BigDecimal productPrice
){}
