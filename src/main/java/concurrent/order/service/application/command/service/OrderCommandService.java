package concurrent.order.service.application.command.service;

import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
import reactor.core.publisher.Mono;

public interface OrderCommandService {

    Mono<OrderEntity> create(OrderEntity entity);

    Mono<OrderEntity> cancel(String orderId);

}
