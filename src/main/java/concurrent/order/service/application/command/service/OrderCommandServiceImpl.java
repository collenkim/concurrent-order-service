package concurrent.order.service.application.command.service;

import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
import concurrent.order.service.infrastructure.rds.repository.OrderItemRepository;
import concurrent.order.service.infrastructure.rds.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCommandServiceImpl implements OrderCommandService{

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * 주문 생성
     *
     * @param entity
     * @param entities
     * @return
     */
    @Transactional
    @Override
    public void createOrder(OrderEntity entity) {
        orderRepository.save(entity);
    }

    /**
     * 주문 취소 명령
     *
     * @param entity
     * @return
     */
    @Transactional
    @Override
    public void cancelOrder(OrderEntity entity) {
        entity.orderCancel();
        //orderRepository.save(entity);
    }

}
