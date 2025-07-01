package concurrent.order.service.domain.model;

import concurrent.order.service.cd.OrderStatus;

import java.math.BigDecimal;
import java.util.Objects;
import lombok.Getter;

import java.util.List;

@Getter
public class Order {

    private final String orderId;
    private final String memberId;
    private final List<OrderItem> items;
    private final OrderStatus status;

    public Order(String orderId, String memberId, List<OrderItem> items) {
        this(orderId, memberId, items, OrderStatus.CREATED);
    }

    public Order(String orderId, String memberId, List<OrderItem> items, OrderStatus status) {
        this.orderId = orderId;
        this.memberId = Objects.requireNonNull(memberId);
        this.status = Objects.requireNonNull(status);
        this.items = Objects.requireNonNull(items);
    }

    public BigDecimal calculateTotalAmount() {
        return items.stream()
                .map(item -> item.getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
