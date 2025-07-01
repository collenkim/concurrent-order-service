package concurrent.order.service.infrastructure.rds.entity;


import concurrent.order.service.cd.OrderStatus;
import jakarta.persistence.*;
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

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order")
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    private OrderEntity(String orderId, OrderStatus status, String userId, Integer totalItemCnt, BigDecimal totalAmount, List<OrderItemEntity> orderItems) {
        this.orderId = orderId;
        this.status = status;
        this.userId = userId;
        this.totalItemCnt = totalItemCnt;
        this.totalAmount = totalAmount;
        this.orderItems = orderItems;
    }

    public static OrderEntity createOrderEntity(String orderId, OrderStatus status, String userId, Integer totalItemCnt, BigDecimal totalAmount, List<OrderItemEntity> orderItems){
        return new OrderEntity(orderId, status, userId, totalItemCnt, totalAmount, orderItems);
    }

    /**
     * 주문 취소
     */
    public void orderCancel(){
        this.status = OrderStatus.CANCELED;
    }

    /**
     * 주문 아이템 추가
     * @param items
     */
    public void addOrderItem(List<OrderItemEntity> items){
        orderItems.addAll(items);
    }
}
