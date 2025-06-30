package concurrent.order.service.application.command.service;

import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import concurrent.order.service.infrastructure.rds.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderCommandServiceImpl implements OrderCommandService{

    private final OrderRepository orderRepository;

    @Transactional
    @Override
    public Mono<OrderEntity> create(OrderEntity entity) {
        return orderRepository.save(entity);
    }

    @Override
    public Mono<OrderEntity> cancel(String orderId) {
        return null;
    }

}
