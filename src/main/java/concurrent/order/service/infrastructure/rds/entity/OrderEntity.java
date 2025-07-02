package concurrent.order.service.infrastructure.rds.entity;


import concurrent.order.service.cd.OrderStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DynamicUpdate
@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false, unique = true, length = 30)
    private String orderId; // 외부 노출용 주문번호 (ORD20240626123456 등)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "user_id", nullable = false, length = 64)
    private String userId;

    @Column(name = "total_item_cnt", nullable = false)
    private Integer totalItemCnt; // 주문 수량

    @Column(name = "original_total_amount", nullable = false)
    private BigDecimal originalTotalAmount; // 원래 총 금액 (할인 전)

    @Column(name = "discount_policy_id", length = 50)
    private String discountPolicyId; // 할인 정책 ID (필수는 아님)

    @Column(name = "discount_amount", nullable = false)
    private BigDecimal discountAmount; // 할인 금액

    @Column(name = "is_free_delivery", nullable = false)
    private boolean isFreeDelivery; // 무료 배송 여부

    @Column(name = "delivery_fee", nullable = false)
    private BigDecimal deliveryFee; // 배송비

    @Column(name = "payment_amount", nullable = false)
    private BigDecimal paymentAmount; // 결제 금액

    @Column(name = "payment_type", length = 20)
    private String paymentType; // 결제 유형 (예: CARD, CASH)

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt; // 주문 취소일자

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    private OrderEntity(String orderId,
                        OrderStatus status,
                        String userId,
                        Integer totalItemCnt,
                        BigDecimal originalTotalAmount,
                        String discountPolicyId,
                        BigDecimal discountAmount,
                        boolean isFreeDelivery,
                        BigDecimal deliveryFee,
                        BigDecimal paymentAmount,
                        String paymentType) {
        this.orderId = orderId;
        this.status = status;
        this.userId = userId;
        this.totalItemCnt = totalItemCnt;
        this.originalTotalAmount = originalTotalAmount;
        this.discountPolicyId = discountPolicyId;
        this.discountAmount = discountAmount;
        this.isFreeDelivery = isFreeDelivery;
        this.deliveryFee = deliveryFee;
        this.paymentAmount = paymentAmount;
        this.paymentType = paymentType;
    }

    public static OrderEntity createOrderEntity(String orderId,
        OrderStatus status,
        String userId,
        Integer totalItemCnt,
        BigDecimal originalTotalAmount,
        String discountPolicyId,
        BigDecimal discountAmount,
        boolean isFreeDelivery,
        BigDecimal deliveryFee,
        BigDecimal paymentAmount,
        String paymentType){
        return new OrderEntity(orderId, status, userId, totalItemCnt, originalTotalAmount,
            discountPolicyId, discountAmount, isFreeDelivery, deliveryFee, paymentAmount, paymentType);
    }

    /**
     * 주문 취소
     */
    public void orderCancel(){
        this.status = OrderStatus.CANCELED;
        this.canceledAt = LocalDateTime.now();
    }

    public boolean isCancel(){
        return this.status == OrderStatus.CANCELED;
    }

    /**
     * 주문 아이템 추가
     * @param items
     */
    public void addOrderItem(List<OrderItemEntity> items){
        items.forEach(item -> item.setOrder(this));
        orderItems.addAll(items);
    }
}
