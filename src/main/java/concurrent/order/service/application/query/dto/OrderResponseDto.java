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

    private String orderId;
    private String memberId;
    private String orderStatus;
    private BigDecimal totalAmount;
    private String deliverType;
    private LocalDateTime createAt;

    private List<OrderItemResponseDto> items;


}
