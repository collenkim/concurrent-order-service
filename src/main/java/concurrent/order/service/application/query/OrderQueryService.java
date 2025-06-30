package concurrent.order.service.application.query;

import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
import reactor.core.publisher.Mono;

public interface OrderQueryService {

    Mono<OrderEntity> getOrderByOrderId(String orderId);

    Mono<OrderItemEntity> getOrderItemByOrderId();

}
