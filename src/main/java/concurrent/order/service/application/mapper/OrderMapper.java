package concurrent.order.service.application.mapper;

import concurrent.order.service.application.command.dto.CreateOrderCommand;
import concurrent.order.service.application.command.dto.OrderCommandResponse;
import concurrent.order.service.application.query.dto.CreateOrderItemResponse;
import concurrent.order.service.domain.model.Order;
import concurrent.order.service.domain.model.OrderItem;
import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
import concurrent.order.service.infrastructure.rds.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    /**
     * Converts a CreateOrderCommand to a domain Order object.
     *
     * @param command the command containing order details
     * @return an Order object
     */
    public static Order toOrderDomain(String orderId, String memberId, CreateOrderCommand command) {
        List<OrderItem> items = command.items().stream()
            .map(itemCmd -> new OrderItem(
                itemCmd.productId(),
                itemCmd.quantity(),
                itemCmd.pricePerItem()
            ))
            .toList();

        return new Order(orderId, memberId, items);
    }

    public static OrderEntity toOrderEntity(Order order) {

        OrderEntity orderEntity = OrderEntity.createOrderEntity(order.getOrderId(), order.getStatus(), order.getMemberId(), order.getItems().size(), new ArrayList<>())

        order.getItems().forEach(item -> item.assignOrder(order)); // 주인 측 설정
        orderEntity.getOrderItems().addAll(orderItems);                 // 읽기용 필드에도 반영
        return order;
        return new OrderEntity(
                order.getOrderId(),
                order.getStatus(),
                order.getMemberId(),
                order.getItems().size(),

        );

        String orderId, OrderStatus status, String userId, Integer totalItemCnt, List<OrderItemEntity> orderItems
    }

    public static List<OrderItemEntity> toOrderItemEntities(Order order) {
        return order.getItems().stream()
                .map(item -> OrderItemEntity.createOrderItem(
                        new ProductEntity(item.getProductId()), // 또는 ProductEntity 참조 조회
                        item.getQuantity(),
                        item.getProductPrice()
                ))
                .toList();
    }

    public static CreateOrderItemResponse toResponse(OrderEntity entity) {
        List<OrderItemEntity> items = entity.getOrderItems().stream()
            .map(item -> new (
                item.getProductId(),
                item.getQuantity(),
                item.getPricePerItem()
            ))
            .toList();

        return new CreateOrderItemResponse(
            entity.getOrderId(),
            entity.getMemberId(),
            entity.getTotalPrice(),
            entity.getStatus().name(),
            items
        );
    }

}
