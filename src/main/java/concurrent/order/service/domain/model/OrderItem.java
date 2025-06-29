package concurrent.order.service.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderItem {

    private final String productId;
    private final int quantity;
    private final BigDecimal pricePerItem;

    public OrderItem(String productId, int quantity, BigDecimal pricePerItem) {
        if (quantity <= 0) throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        if (pricePerItem.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("가격은 0보다 커야 합니다.");

        this.productId = Objects.requireNonNull(productId);
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
    }


}
