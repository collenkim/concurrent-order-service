package concurrent.order.service.domain.model;

import java.util.Objects;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderItem {

    private final String productId;       // 상품 ID
    private final int quantity;           // 수량
    private final BigDecimal productPrice; // 단일 상품 가격

    public OrderItem(String productId, int quantity, BigDecimal productPrice) {
        if (Objects.requireNonNull(quantity) <= 0) {
            throw new IllegalArgumentException("상품 수량은 1 이상이어야 합니다.");
        }
        if (Objects.requireNonNull(productPrice).compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("상품 가격은 0보다 커야 합니다.");
        }
        this.productId = Objects.requireNonNull(productId, "상품 ID는 null일 수 없습니다.");
        this.quantity = quantity;
        this.productPrice = productPrice;
    }

    /**
     * 총 금액 = 수량 * 단가
     */
    public BigDecimal calculateTotalPrice() {
        return productPrice.multiply(BigDecimal.valueOf(quantity));
    }

}
