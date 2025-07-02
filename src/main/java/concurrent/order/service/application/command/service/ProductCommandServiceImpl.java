package concurrent.order.service.application.command.service;

import static concurrent.order.service.application.constants.OrderConstants.PRODUCT_STOCK_EXCEEDED_MSG;

import concurrent.order.service.exception.ProductStockExceededException;
import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
import concurrent.order.service.infrastructure.rds.entity.ProductEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductCommandServiceImpl implements ProductCommandService {

    /**
     * 재고 차감
     *
     * @param entities
     * @return
     */
    @Transactional
    @Override
    public void decreaseProductStockByOrderItems(List<OrderItemEntity> entities){

        for (OrderItemEntity item : entities) {
            ProductEntity product = item.getProduct();

            int currentStock = product.getStockQuantity(); // 가정: stockQuantity 필드 존재
            int orderedQuantity = item.getQuantity();

            if (currentStock < orderedQuantity) {
                throw new ProductStockExceededException(String.format(PRODUCT_STOCK_EXCEEDED_MSG, product.getProductId(), orderedQuantity, currentStock));
            }

            product.decreaseStock(orderedQuantity);
        }
    }

}
