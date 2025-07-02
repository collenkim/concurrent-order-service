package concurrent.order.service.domain.model;

import concurrent.order.service.exception.InvalidOrderValueException;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderItem {

    private final String productId;       // 상품 ID
    private final int quantity;           // 수량
    private final BigDecimal productPrice; // 단일 상품 가격

    public OrderItem(String productId, int quantity, BigDecimal productPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.productPrice = productPrice;
        this.validate();
    }

    public void validate() {
        if (productId == null || productId.isBlank()) {
            throw new InvalidOrderValueException("상품 ID는 필수입니다.");
        }
        if (productPrice == null || productPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOrderValueException("상품 가격이 유효하지 않습니다.");
        }
        if (quantity <= 0) {
            throw new InvalidOrderValueException("상품 수량이 0 이하일 수 없습니다.");
        }
    }

}
