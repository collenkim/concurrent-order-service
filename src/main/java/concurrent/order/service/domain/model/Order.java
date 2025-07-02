package concurrent.order.service.domain.model;

import concurrent.order.service.cd.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;

import java.util.List;
import lombok.Setter;

@Getter
@Setter
public class Order {

    private final String orderId; // 주문 번호
    private final String memberId; // 주문자 ID
    private final List<OrderItem> items; // 주문 상품 목록
    private final OrderStatus status; // 주문 상태
    private final BigDecimal originalTotalAmount; // 원래 총 금액

    private LocalDateTime orderedAt; // 주문 날짜
    private LocalDateTime canceledAt; // 주문 취소 날짜
    private String discountPolicyId; // 할인 정책 ID
    private BigDecimal discountAmount; // 할인 금액
    private BigDecimal paymentAmount; // 결제 금액
    private boolean isFreeDelivery; // 무료 배송 여부
    private BigDecimal deliveryFee; // 배송비
    private String paymentType; // 결제 타입 (신용카드, 계좌이체 등)

    public Order(String orderId, String memberId, List<OrderItem> items) {
        this(orderId, memberId, items, OrderStatus.CREATED);
    }

    public Order(String orderId, String memberId, List<OrderItem> items, OrderStatus status) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.status = status;
        this.items = items;
        this.originalTotalAmount = calculateTotalAmount();
        this.paymentAmount = originalTotalAmount; // 초기 결제 금액은 원래 총 금액
    }

    public BigDecimal calculateTotalAmount() {
        return items.stream()
                .map(item -> item.getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void calculateDiscountAmount() {
        BigDecimal discountedAmount = originalTotalAmount;

        if (discountAmount != null && discountAmount.compareTo(BigDecimal.ZERO) > 0) {
            discountedAmount = discountedAmount.subtract(discountAmount);
        }

        BigDecimal total = discountedAmount.add(deliveryFee != null ? deliveryFee : BigDecimal.ZERO);

        this.paymentAmount = total.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : total;
    }

    public void validate() {

        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("주문 ID는 필수입니다.");
        }

        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("주문 ID는 필수입니다.");
        }

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("주문 상품이 비어있습니다.");
        }

        if (discountAmount != null && discountAmount.compareTo(BigDecimal.ZERO) > 0) {
            if (discountPolicyId == null || discountPolicyId.isBlank()) {
                throw new IllegalArgumentException("할인 정책 ID는 필수입니다.");
            }
        }
    }

}
