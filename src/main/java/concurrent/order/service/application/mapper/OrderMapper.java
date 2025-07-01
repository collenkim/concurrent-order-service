package concurrent.order.service.application.mapper;

import concurrent.order.service.application.command.dto.CreateOrderDto;
import concurrent.order.service.application.query.dto.OrderResponseDto;
import concurrent.order.service.domain.model.Order;
import concurrent.order.service.domain.model.OrderItem;
import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    /**
     * Converts a CreateOrderCommand to a domain Order object.
     *
     * @param command the command containing order details
     * @return an Order object
     */
    public static Order toDomain(String orderId, CreateOrderDto command) {
        List<OrderItem> items = command.items().stream()
            .map(itemCmd -> new OrderItem(
                itemCmd.productId(),
                itemCmd.quantity(),
                itemCmd.productPrice()
            ))
            .toList();

        return new Order(orderId, command.userId(), items);
    }

    /**
     * Converts an Order object to an OrderEntity.
     *
     * @param order the Order object
     * @return an OrderEntity object
     */
    public static OrderEntity toEntity(Order order) {
        return OrderEntity.createOrderEntity(order.getOrderId(), order.getStatus(), order.getMemberId(), order.getItems().size(), order.calculateTotalAmount(), new ArrayList<>());
    }

    /**
     * Converts an OrderEntity to an OrderResponseDto.
     *
     * @param orderEntity the OrderEntity to convert
     * @return an OrderResponseDto object
     */
    public static OrderResponseDto toResponseDto(OrderEntity orderEntity) {
        return new OrderResponseDto(
            orderEntity.getOrderId(),
                orderEntity.getUserId(),
                orderEntity.getStatus().getName(),
                orderEntity.getTotalAmount(),
                orderEntity.getCreateDt(),
                OrderItemMapper.toResponseDtoList(orderEntity.getOrderItems())
        );

    }

}
