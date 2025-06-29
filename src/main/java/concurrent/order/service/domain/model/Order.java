package concurrent.order.service.domain.model;

import concurrent.order.service.cd.OrderStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class Order {

    private final String id;
    private final String memberId;
    private final List<OrderItem> items;
    private OrderStatus status;

    public Order(String memberId, List<OrderItem> items) {
        this(UUID.randomUUID().toString(), memberId, items, OrderStatus.CREATED);
    }

    public Order(String id, String productId, int quantity, BigDecimal price, String memberId, OrderStatus status) {
        if (quantity <= 0) throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        if (price.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("금액은 0보다 커야 합니다.");

        this.id = id;
        this.productId = Objects.requireNonNull(productId);
        this.quantity = quantity;
        this.price = Objects.requireNonNull(price);
        this.memberId = Objects.requireNonNull(memberId);
        this.status = Objects.requireNonNull(status);
    }

    public void cancel() {
        if (status == OrderStatus.CANCELED) {
            throw new IllegalStateException("이미 취소된 주문입니다.");
        }
        status = OrderStatus.CANCELED;
    }

}
