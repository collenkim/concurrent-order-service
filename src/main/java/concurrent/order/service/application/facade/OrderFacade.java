package concurrent.order.service.application.facade;

import concurrent.order.service.application.command.dto.CreateOrderCommand;
import concurrent.order.service.application.command.dto.CreateOrderItemCommand;
import concurrent.order.service.application.command.dto.OrderCommandResponse;
import concurrent.order.service.application.mapper.OrderMapper;
import concurrent.order.service.application.command.service.OrderCommandService;
import concurrent.order.service.application.query.OrderQueryService;
import concurrent.order.service.application.query.ProductQueryService;
import concurrent.order.service.application.query.dto.OrderResponse;
import concurrent.order.service.domain.model.Order;
import concurrent.order.service.generator.OrderIdGenerator;
import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
import concurrent.order.service.infrastructure.rds.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;
    private final ProductQueryService productQueryService;

    /**
     * 주문 생성
     *
     * @param request
     * @return
     */
    public Mono<OrderResponse> createOrder(CreateOrderCommand request) {

        //Order Id 생성
        final String orderId = OrderIdGenerator.getGenerateOrderId();

        Order order = OrderMapper.toOrderDomain(orderId, "test123", request);

        List<String> productIds = request.items().stream()
                .map(CreateOrderItemCommand::productId)
                .toList();

        //order.applyDiscount(request.discountRate()); // 할인 적용
        //order.validate(); // 유효성 검사

        return Mono.fromCallable(() -> productQueryService.getProductsByIds(productIds))
            .subscribeOn(Schedulers.boundedElastic())
            .flatMap(products -> {
                OrderEntity entity = OrderMapper.toOrderEntity(order);
                List<OrderItemEntity> entities = OrderMapper.toOrderItemEntities(order, entity, products);
                entity.addOrderItem(entities);

                // createOrder (JPA 저장) 호출도 감싸기
                return Mono.fromRunnable(() -> orderCommandService.createOrder(entity, entities))
                    .subscribeOn(Schedulers.boundedElastic())
                    .then(
                        // 조회 후 응답 매핑
                        Mono.fromCallable(() -> orderQueryService.getOrder(orderId))
                            .subscribeOn(Schedulers.boundedElastic())
                            .map(OrderMapper::toOrderResponse)
                    );
            });
    }

}
