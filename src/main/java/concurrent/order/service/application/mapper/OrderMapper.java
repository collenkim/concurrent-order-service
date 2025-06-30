package concurrent.order.service.application.mapper;

import concurrent.order.service.application.command.dto.CreateOrderCommand;
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

    public static OrderEntity toEntity(Order domain) {
        List<OrderItemEntity> itemEntities = domain.getItems().stream()
            .map(i -> new OrderItemEntity(null, i.productId(), i.quantity(), i.price()))
            .toList();
        return OrderEntity.createOrder().builder()
            .orderId(domain.getOrderId())
            .userId(domain.getUserId())
            .totalAmount(domain.totalAmount())
            .status("CREATED")
            .items(itemEntities)
            .build();
    }

}
