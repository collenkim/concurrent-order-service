package concurrent.order.service.application.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * 주문 생성 요청 Dto
 *  - 정식 서비스에서 유저 ID는 인증 헤더의 JwtToken에서 가져오거나 Session에서 가져와서 사용하도록 처리해야함
 *
 * @param userId
 * @param items
 */
public record CreateOrderDto(
        @NotBlank(message = "유저 ID는 필수 입니다.")
        String userId,
        @NotEmpty(message = "주문 항목은 하나 이상이어야 합니다.")
        List<CreateOrderItemDto> items
){}
