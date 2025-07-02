package concurrent.order.service.application.mapper;

import concurrent.order.service.application.command.dto.CreateOrderDto;
import concurrent.order.service.application.query.dto.OrderResponseDto;
import concurrent.order.service.domain.model.Order;
import concurrent.order.service.domain.model.OrderItem;
import concurrent.order.service.infrastructure.kafka.dto.OrderCreatedEventDto;
import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import java.math.BigDecimal;
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

        Order order = new Order(orderId, command.userId(), items);

        order.setDiscountPolicyId(command.discountPolicyId());
        order.setDiscountAmount(command.discountAmount());
        order.setDeliveryFee(command.deliveryFee());
        order.setPaymentAmount(command.paymentAmount());
        order.setPaymentType(command.paymentType());
        order.setFreeDelivery(order.getDeliveryFee() != null && order.getDeliveryFee().compareTo(BigDecimal.ZERO) == 0);

        return order;
    }

    /**
     * Converts an Order object to an OrderEntity.
     *
     * @param order the Order object
     * @return an OrderEntity object
     */
    public static OrderEntity toEntity(Order order) {
        return OrderEntity.createOrderEntity(order.getOrderId(),
            order.getStatus(),
            order.getMemberId(),
            order.getItems().size(),
            order.getOriginalTotalAmount(),
            order.getDiscountPolicyId(), // null 허용
            order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO,
            order.isFreeDelivery(),
            order.getDeliveryFee() != null ? order.getDeliveryFee() : BigDecimal.ZERO,
            order.getPaymentAmount() != null ? order.getPaymentAmount() : order.getOriginalTotalAmount(),
            order.getPaymentType()); // null 허용);
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
                orderEntity.getPaymentAmount(),
                orderEntity.getCreateDt(),
                orderEntity.getCanceledAt(),
                OrderItemMapper.toResponseDtoList(orderEntity.getOrderItems())
        );

    }

    /**
     *
     * @param order
     * @return
     */
    public static OrderCreatedEventDto toOrderCreatedEvent(OrderEntity order) {
        return OrderCreatedEventDto.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .paymentAmount(order.getPaymentAmount())
                .createdAt(order.getCreateDt())
                .build();
    }

}
