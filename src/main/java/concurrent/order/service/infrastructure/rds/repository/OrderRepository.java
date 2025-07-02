package concurrent.order.service.infrastructure.rds.repository;

import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByOrderId(String orderId);

    @Query("SELECT o FROM OrderEntity o JOIN FETCH o.orderItems oi JOIN FETCH oi.product WHERE o.orderId = :orderId")
    Optional<OrderEntity> findByOrderIdWithItemsAndProducts(@Param("orderId") String orderId);
}
