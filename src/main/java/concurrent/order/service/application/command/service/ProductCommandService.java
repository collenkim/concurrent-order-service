package concurrent.order.service.application.command.service;

import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
import java.util.List;

public interface ProductCommandService {

    /**
     * 재고 차감
     *
     * @param entities
     * @return
     */
    void decreaseProductStockByOrderItems(List<OrderItemEntity> entities);

}
