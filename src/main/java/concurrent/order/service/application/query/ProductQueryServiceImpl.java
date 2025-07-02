package concurrent.order.service.application.query;

import concurrent.order.service.infrastructure.rds.entity.ProductEntity;
import concurrent.order.service.infrastructure.rds.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductQueryServiceImpl implements ProductQueryService{

    private final ProductRepository productRepository;

    /**
     * 상품 ID 목록으로 상품 정보 조회
     * @param productIds
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<ProductEntity> getProductsByIds(List<String> productIds){
        return productRepository.findByProductIdIn(productIds);
    }

}
