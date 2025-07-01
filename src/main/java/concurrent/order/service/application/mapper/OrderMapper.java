package concurrent.order.service.application.mapper;

import concurrent.order.service.application.command.dto.CreateOrderCommandDto;
import concurrent.order.service.application.query.dto.OrderResponseDto;
import concurrent.order.service.domain.model.Order;
import concurrent.order.service.domain.model.OrderItem;
import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
import concurrent.order.service.infrastructure.rds.entity.ProductEntity;

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
    public static Order toOrderDomain(String orderId, String memberId, CreateOrderCommandDto command) {
        List<OrderItem> items = command.items().stream()
            .map(itemCmd -> new OrderItem(
                itemCmd.productId(),
                itemCmd.quantity(),
                itemCmd.productPrice()
            ))
            .toList();

        return new Order(orderId, memberId, items);
    }

    public static OrderEntity toOrderEntity(Order order) {
        return OrderEntity.createOrderEntity(order.getOrderId(), order.getStatus(), order.getMemberId(), order.getItems().size(), new ArrayList<>());
    }

    public static List<OrderItemEntity> toOrderItemEntities(Order order, OrderEntity orderEntity, List<ProductEntity> products) {
        Map<String, ProductEntity> productMap = products.stream()
            .collect(Collectors.toMap(ProductEntity::getProductId, Function.identity()));

        return order.getItems().stream()
            .map(item -> {
                ProductEntity product = productMap.get(item.getProductId());
                if (product == null) {
                    throw new IllegalArgumentException("Product not found for id: " + item.getProductId());
                }
                return OrderItemEntity.createOrderItem(
                    orderEntity,
                    product,
                    item.getQuantity(),
                    item.getProductPrice()
                );
            })
            .toList();
    }

    public static OrderResponseDto toOrderResponse(OrderEntity orderEntity) {
        return new OrderResponseDto(
            orderEntity.getOrderId()
        );

    }
}
