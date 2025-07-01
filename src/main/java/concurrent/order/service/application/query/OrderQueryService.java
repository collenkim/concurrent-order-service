package concurrent.order.service.application.query;

import concurrent.order.service.infrastructure.rds.entity.OrderEntity;

public interface OrderQueryService {

    /**
     * 주문정보 조회
     *
     * @param orderId
     * @return
     */
    OrderEntity getOrder(String orderId);

    //Mono<OrderDetailResponse> getOrderDetail(String orderId);
}
