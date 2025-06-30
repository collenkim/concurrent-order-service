package concurrent.order.service.application.mapper;

import concurrent.order.service.application.command.dto.CreateOrderCommand;
import concurrent.order.service.application.command.dto.OrderCommandResponse;
import concurrent.order.service.application.query.dto.CreateOrderItemResponse;
import concurrent.order.service.domain.model.Order;
import concurrent.order.service.domain.model.OrderItem;
import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
import java.util.List;

public class OrderMapper {

    /**
     * Converts a CreateOrderCommand to a domain Order object.
     *
     * @param command the command containing order details
     * @return an Order object
     */
    public static Order toDomain(String orderId, String memberId, CreateOrderCommand command) {
        List<OrderItem> items = command.items().stream()
            .map(itemCmd -> new OrderItem(
                itemCmd.productId(),
                itemCmd.quantity(),
                itemCmd.pricePerItem()
            ))
            .toList();

        return new Order(orderId, memberId, items);
    }

    public static OrderEntity toEntity(String orderId, String memberId, CreateOrderCommand command) {
        List<OrderItemEntity> items = command.items().stream()
            .map(itemCmd -> new OrderItem(
                itemCmd.productId(),
                itemCmd.quantity(),
                itemCmd.pricePerItem()
            ))
            .toList();

        return new OrderEntity(orderId, memberId, items);
    }

    public static CreateOrderItemResponse toResponse(OrderEntity entity) {
        List<OrderItemEntity> items = entity.getOrderItems().stream()
            .map(item -> new OrderItemResponse(
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
