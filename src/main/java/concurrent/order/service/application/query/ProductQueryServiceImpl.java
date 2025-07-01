package concurrent.order.service.application.query;

import concurrent.order.service.infrastructure.rds.entity.ProductEntity;
import concurrent.order.service.infrastructure.rds.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductQueryServiceImpl implements ProductQueryService{

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ProductEntity> getProductsByIds(List<String> productIds){
        return productRepository.findByProductIdIn(productIds);
    }

}
