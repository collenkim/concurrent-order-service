package concurrent.order.service.application.command.service;

import concurrent.order.service.infrastructure.rds.entity.OrderEntity;

public interface OrderCommandService {

    /**
     * 주문 생성 명령
     *
     * @param entity
     * @return
     */
    void createOrder(OrderEntity entity);

    /**
     * 주문 취소 명령
     *
     * @param entity
     * @return
     */
    void cancelOrder(OrderEntity entity);

}
