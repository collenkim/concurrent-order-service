package concurrent.order.service.application.mapper;

import concurrent.order.service.application.command.dto.CreateOrderDto;
import concurrent.order.service.application.query.dto.OrderResponseDto;
import concurrent.order.service.domain.model.Order;
import concurrent.order.service.domain.model.OrderItem;
import concurrent.order.service.infrastructure.mongo.document.OrderHistDocument;
import concurrent.order.service.infrastructure.mongo.document.OrderHistDocument.OrderItemLog;
import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
        order.setFreeDelivery(order.getDeliveryFee().compareTo(BigDecimal.ZERO) == 0);

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

    public static OrderHistDocument toDocument(OrderEntity orderEntity) {
        return OrderHistDocument.builder()
            .orderId(orderEntity.getOrderId())
            .memberId(orderEntity.getUserId())
            .status(orderEntity.getStatus().name())
            .originalTotalAmount(orderEntity.getOriginalTotalAmount())
            .discountPolicyId(orderEntity.getDiscountPolicyId())
            .discountAmount(orderEntity.getDiscountAmount())
            .deliveryFee(orderEntity.getDeliveryFee())
            .isFreeDelivery(orderEntity.isFreeDelivery())
            .paymentAmount(orderEntity.getPaymentAmount())
            .paymentType(orderEntity.getPaymentType())
            .orderedAt(orderEntity.getCreateDt())
            .canceledAt(orderEntity.getCanceledAt())
            .items(toItemLogs(orderEntity.getOrderItems()))
            .build();
    }

    private static List<OrderItemLog> toItemLogs(List<OrderItemEntity> entities) {
        return entities.stream()
            .map(item -> new OrderItemLog(
                item.getProduct().getProductId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getProduct().getPrice(),
                item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
            ))
            .collect(Collectors.toList());
    }

}
