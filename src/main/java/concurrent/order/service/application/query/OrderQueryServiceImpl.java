package concurrent.order.service.application.query;

import static concurrent.order.service.application.constants.OrderConstants.NOT_FOUND_ORDER_MSG;

import concurrent.order.service.exception.NotFoundOrderException;
import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import concurrent.order.service.infrastructure.rds.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderRepository orderRepository;

    /**
     * 주문정보 조회
     * @param orderId
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public OrderEntity getOrder(String orderId) {
        return orderRepository.findByOrderId(orderId).orElseThrow(() -> new NotFoundOrderException(String.format(NOT_FOUND_ORDER_MSG, orderId)));
    }

    /**
     * 주문정보 조회 (주문 아이템과 상품 정보 포함)
     * @param orderId
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public OrderEntity getOrderWithItemsAndProducts(String orderId) {
        return orderRepository.findByOrderIdWithItemsAndProducts(orderId).orElseThrow(() -> new NotFoundOrderException(String.format(NOT_FOUND_ORDER_MSG, orderId)));
    }

}
