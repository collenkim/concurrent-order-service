package concurrent.order.service.application.command.service;

import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OrderCommandService {

    /**
     * 주문 생성 명령
     *
     * @param entity
     * @param entities
     * @return
     */
    void createOrder(OrderEntity entity, List<OrderItemEntity> entities);

    /**
     * 주문 취소 명령
     *
     * @param orderId
     * @return
     */
    void cancelOrder(String orderId);

}
