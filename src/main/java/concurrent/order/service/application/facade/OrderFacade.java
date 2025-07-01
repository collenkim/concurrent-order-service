package concurrent.order.service.application.facade;

import concurrent.order.service.application.command.dto.CreateOrderDto;
import concurrent.order.service.application.command.dto.CreateOrderItemDto;
import concurrent.order.service.application.mapper.OrderMapper;
import concurrent.order.service.application.command.service.OrderCommandService;
import concurrent.order.service.application.query.OrderQueryService;
import concurrent.order.service.application.query.ProductQueryService;
import concurrent.order.service.application.query.dto.OrderResponseDto;
import concurrent.order.service.domain.model.Order;
import concurrent.order.service.generator.OrderIdGenerator;
import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
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
     *  - 상품 정보 조회는 MSA 환겨에서라면 상품 서비스 조회 요청을 통해 데이터를 가져와야함
     *  - userId는 세션에서 가져오거나 인증 토큰 (JWT 등)에서 추출해야함
     *
     * @param request
     * @return
     */
    public Mono<OrderResponseDto> createOrder(CreateOrderDto request) {

        //Order Id 생성
        final String orderId = OrderIdGenerator.getGenerateOrderId();

        //유저 ID 임시 등록
        String tempUserId = "test1234";
        Order order = OrderMapper.toOrderDomain(orderId, tempUserId, request);

        List<String> productIds = request.items().stream()
                .map(CreateOrderItemDto::productId)
                .toList();

        //order.applyDiscount(request.discountRate()); // 할인 적용
        //order.validate(); // 유효성 검사

        return Mono.fromCallable(() -> productQueryService.getProductsByIds(productIds)) // 상품 정보 조회
            .subscribeOn(Schedulers.boundedElastic())
            .flatMap(products -> {
                OrderEntity entity = OrderMapper.toOrderEntity(order);
                List<OrderItemEntity> entities = OrderMapper.toOrderItemEntities(order, entity, products);
                entity.addOrderItem(entities);

                return Mono.fromRunnable(() -> orderCommandService.createOrder(entity, entities))
                    .subscribeOn(Schedulers.boundedElastic())
                    .then(
                        Mono.fromCallable(() -> orderQueryService.getOrder(orderId))
                            .subscribeOn(Schedulers.boundedElastic())
                            .map(OrderMapper::toOrderResponse)
                    );
            });
    }

}
