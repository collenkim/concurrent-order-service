package concurrent.order.service.application.query;

import concurrent.order.service.infrastructure.rds.entity.ProductEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductQueryService {

    List<ProductEntity> getProductsByIds(List<String> productIds);

}
