package concurrent.order.service.infrastructure.rds.repository;

import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;


public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByOrderId(String orderId);

    @Query("SELECT o FROM OrderEntity o JOIN FETCH o.orderItems WHERE o.orderId = :orderId")
    Optional<OrderEntity> findByOrderIdWithItems(String orderId);
}
