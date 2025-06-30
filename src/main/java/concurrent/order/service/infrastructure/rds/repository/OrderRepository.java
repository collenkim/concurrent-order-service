package concurrent.order.service.infrastructure.rds.repository;

import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface OrderRepository extends ReactiveCrudRepository<OrderEntity, Long> {

    Mono<OrderEntity> findByOrderId(String orderId);


}
