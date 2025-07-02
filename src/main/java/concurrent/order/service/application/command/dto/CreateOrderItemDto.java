package concurrent.order.service.application.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * 주문 항목 생성 요청 Dto
 *  - 상품 ID, 수량, 상품 가격을 포함
 *
 * @param productId   상품 ID
 * @param quantity    수량
 * @param productPrice 상품 가격
 */
public record CreateOrderItemDto(
        @NotBlank(message = "상품 ID는 필수입니다.")
        String productId,

        @Positive(message = "수량은 1 이상이어야 합니다.")
        Integer quantity,

        @Positive(message = "상품 가격은 0보다 커야 합니다.")
        BigDecimal productPrice
){}
