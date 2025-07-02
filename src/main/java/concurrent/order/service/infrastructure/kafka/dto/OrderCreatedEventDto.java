package concurrent.order.service.infrastructure.kafka.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class OrderCreatedEventDto {
    private String orderId;
    private String userId;
    private BigDecimal paymentAmount;
    private LocalDateTime createdAt;
}
