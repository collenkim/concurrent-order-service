package concurrent.order.service.application.command.service;

import concurrent.order.service.application.command.dto.OrderCommandRequest;
import concurrent.order.service.cd.OrderStatus;
import concurrent.order.service.domain.model.Order;
import concurrent.order.service.domain.service.OrderDomainService;
import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class OrderCommandService {

    private final OrderCommandRepository commandRepository;
    private final OrderDomainService domainService; // 도메인 서비스 호출

    public Mono<Order> createOrder(OrderCommandRequest request) {
        Order order = new Order(UUID.randomUUID().toString(), request.getProductId(), request.getQuantity(), OrderStatus.CREATED);
        // 도메인 서비스에서 비즈니스 규칙 검증 등
        domainService.validateNewOrder(order);

        // Entity 변환 후 저장
        OrderEntity entity = OrderMapper.toEntity(order);
        return commandRepository.save(entity)
                .map(OrderMapper::toDomain);
    }

}
