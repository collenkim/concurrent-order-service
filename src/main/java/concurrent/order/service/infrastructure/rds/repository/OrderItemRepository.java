package concurrent.order.service.infrastructure.rds.repository;

import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

}
