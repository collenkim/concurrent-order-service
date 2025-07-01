package concurrent.order.service.infrastructure.rds.repository;

import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByOrderId(String orderId);

}
