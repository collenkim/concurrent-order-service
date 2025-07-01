package concurrent.order.service.initializer;

import concurrent.order.service.cd.ProductStatus;
import concurrent.order.service.infrastructure.rds.entity.ProductEntity;
import concurrent.order.service.infrastructure.rds.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final ProductRepository productRepository;

    @PostConstruct
    @Transactional
    public void insertInitialProducts() {
        if (productRepository.count() == 0) {
            List<ProductEntity> products = List.of(
                    ProductEntity.createProductEntity("PRD_240701120000001_0001_101", "CAT001", "상품A", "상품A 설명입니다", ProductStatus.SELLING, 100, new BigDecimal("19900")),
                    ProductEntity.createProductEntity("PRD_240701120000002_0001_102", "CAT001", "상품B", "상품B 설명입니다", ProductStatus.STOPPED, 50, new BigDecimal("29900")),
                    ProductEntity.createProductEntity("PRD_240701120000003_0001_103", "CAT002", "상품C", "상품C 설명입니다", ProductStatus.SOLD_OUT, 0, new BigDecimal("9900"))
            );
            productRepository.saveAll(products);
        }
    }

}
