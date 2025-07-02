package concurrent.order.service.infrastructure.mongo.document;

import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "order_history")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderHistDocument {

    @Id
    private String id;

    private String orderId;
    private String memberId;
    private String status;

    private BigDecimal originalTotalAmount; // 원금
    private String discountPolicyId;
    private BigDecimal discountAmount;
    private BigDecimal discountRate; // 할인율 (옵션)
    private BigDecimal deliveryFee;
    private boolean isFreeDelivery;
    private BigDecimal paymentAmount;

    private String paymentType;

    private LocalDateTime orderedAt;
    private LocalDateTime canceledAt;

    private List<OrderItemLog> items;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class OrderItemLog {
        private String productId;
        private String productName;
        private int quantity;
        private BigDecimal price; // 단가
        private BigDecimal totalAmount;
    }

}
