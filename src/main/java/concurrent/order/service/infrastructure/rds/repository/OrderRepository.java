package concurrent.order.service.infrastructure.rds.repository;

import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import reactor.core.publisher.Mono;


public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    OrderEntity findByOrderId(String orderId);

}
