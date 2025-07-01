package concurrent.order.service.application.mapper;

import concurrent.order.service.application.command.dto.CreateOrderDto;
import concurrent.order.service.application.query.dto.OrderItemResponseDto;
import concurrent.order.service.application.query.dto.OrderResponseDto;
import concurrent.order.service.domain.model.Order;
import concurrent.order.service.domain.model.OrderItem;
import concurrent.order.service.exception.NotExistsProductException;
import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
import concurrent.order.service.infrastructure.rds.entity.ProductEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderMapper {

    /**
     * Converts a CreateOrderCommand to a domain Order object.
     *
     * @param command the command containing order details
     * @return an Order object
     */
    public static Order toOrderDomain(String orderId, String memberId, CreateOrderDto command) {
        List<OrderItem> items = command.items().stream()
            .map(itemCmd -> new OrderItem(
                itemCmd.productId(),
                itemCmd.quantity(),
                itemCmd.productPrice()
            ))
            .toList();

        return new Order(orderId, memberId, items);
    }

    /**
     * Converts an Order object to an OrderEntity.
     *
     * @param order the Order object
     * @return an OrderEntity object
     */
    public static OrderEntity toOrderEntity(Order order) {
        return OrderEntity.createOrderEntity(order.getOrderId(), order.getStatus(), order.getMemberId(), order.getItems().size(), new ArrayList<>());
    }

    /**
     * Converts an Order and its associated products to a list of OrderItemEntity objects.
     *
     * @param order
     * @param orderEntity
     * @param products
     * @return
     */
    public static List<OrderItemEntity> toOrderItemEntities(Order order, OrderEntity orderEntity, List<ProductEntity> products) {
        Map<String, ProductEntity> productMap = products.stream()
            .collect(Collectors.toMap(ProductEntity::getProductId, Function.identity()));

        return order.getItems().stream()
            .map(item -> {
                ProductEntity product = productMap.get(item.getProductId());
                return OrderItemEntity.createOrderItem(
                    orderEntity,
                    product,
                    item.getQuantity(),
                    item.getProductPrice()
                );
            })
            .toList();
    }

    /**
     * Converts an OrderEntity to an OrderResponseDto.
     *
     * @param orderEntity the OrderEntity to convert
     * @return an OrderResponseDto object
     */
    public static OrderResponseDto toOrderResponse(OrderEntity orderEntity) {

        private String orderId;
        private String memberId;
        private String orderStatus;
        private BigDecimal totalAmount;
        private String deliverType;
        private LocalDateTime createAt;
        private List<OrderItemResponseDto> items;

        return new OrderResponseDto(
            orderEntity.getOrderId()
        );

    }
}
