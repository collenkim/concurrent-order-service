package concurrent.order.service.application.command.service;

import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
import concurrent.order.service.infrastructure.rds.repository.OrderItemRepository;
import concurrent.order.service.infrastructure.rds.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
    public Mono<Void> createOrder(OrderEntity entity, List<OrderItemEntity> entities) {
        return Flux.fromIterable(entity.getOrderItems())
            .concatMap(item -> Mono.fromCallable(() -> orderItemRepository.save(item))
                .subscribeOn(Schedulers.boundedElastic()))
            .collectList()
            .flatMap(savedItems -> Mono.fromCallable(() -> {
                entity.addOrderItem(savedItems);
                return orderRepository.save(entity);
            }).subscribeOn(Schedulers.boundedElastic()))
            .then();
    }

    /**
     * 주문 취소 명령
     *
     * @param orderId
     * @return
     */
    @Override
    public Mono<Void> cancelOrder(String orderId) {
        return null;
    }

}
