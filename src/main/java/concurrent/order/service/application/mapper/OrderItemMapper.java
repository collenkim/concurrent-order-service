package concurrent.order.service.application.mapper;

import concurrent.order.service.application.query.dto.OrderItemResponseDto;
import concurrent.order.service.domain.model.Order;
import concurrent.order.service.infrastructure.mongo.document.OrderHistDocument.OrderItemLog;
import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
import concurrent.order.service.infrastructure.rds.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderItemMapper {

    /**
     * Converts an OrderItemEntity to an OrderItemResponseDto.
     *
     * @param entity
     * @return
     */
    public static OrderItemResponseDto toResponseDto(OrderItemEntity entity) {
        return new OrderItemResponseDto(
                entity.getProduct().getProductId(),
                entity.getProduct().getName(),
                entity.getQuantity(),
                entity.getPrice()
        );
    }

    /**
     * Converts a list of OrderItemEntity objects to a list of OrderItemResponseDto objects.
     *
     * @param entities
     * @return
     */
    public static List<OrderItemResponseDto> toResponseDtoList(List<OrderItemEntity> entities) {
        return entities.stream()
                .map(OrderItemMapper::toResponseDto)
                .toList();
    }

    /**
     * Converts an Order and its associated products to a list of OrderItemEntity objects.
     *
     * @param order
     * @param orderEntity
     * @param products
     * @return
     */
    public static List<OrderItemEntity> toEntities(Order order, OrderEntity orderEntity, List<ProductEntity> products) {
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
     * Converts an OrderItemEntity and its associated ProductEntity to an OrderItemLog.
     *
     * @param item
     * @param product
     * @return
     */
    public static OrderItemLog toOrderItemLog(OrderItemEntity item, ProductEntity product) {
        BigDecimal total = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        return OrderItemLog.builder()
            .productId(product.getProductId())
            .productName(product.getName())
            .quantity(item.getQuantity())
            .price(item.getPrice())
            .totalAmount(total)
            .build();
    }

}
