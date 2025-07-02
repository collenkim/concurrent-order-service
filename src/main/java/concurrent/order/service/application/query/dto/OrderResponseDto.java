package concurrent.order.service.application.query.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class OrderResponseDto {

    private String orderId; //주문 ID
    private String memberId; //회원 ID
    private String orderStatus; //주문 상태 (예: 주문 접수, 결제 완료, 배송 중, 배송 완료 등)
    private BigDecimal totalAmount; //총 주문 금액
    private LocalDateTime createAt; //주문 일시
    private LocalDateTime cancelAt; //주문 취소 일시 (주문이 취소된 경우에만 값이 존재)
    private List<OrderItemResponseDto> items; //주문 상품 목록
    
}
