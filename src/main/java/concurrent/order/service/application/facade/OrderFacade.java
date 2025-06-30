package concurrent.order.service.application.facade;

import concurrent.order.service.application.command.dto.CreateOrderCommand;
import concurrent.order.service.application.command.dto.CreateOrderItemCommand;
import concurrent.order.service.application.command.dto.OrderCommandResponse;
import concurrent.order.service.application.mapper.OrderMapper;
import concurrent.order.service.application.command.service.OrderCommandService;
import concurrent.order.service.application.query.OrderQueryService;
import concurrent.order.service.application.query.ProductQueryService;
import concurrent.order.service.domain.model.Order;
import concurrent.order.service.generator.OrderIdGenerator;
import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

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
    public Mono<OrderCommandResponse> createOrder(CreateOrderCommand request) {

        //Order Id 생성
        final String orderId = OrderIdGenerator.getGenerateOrderId();

        Order order = OrderMapper.toOrderDomain(orderId, "test123", request);

        List<String> productIds = request.items().stream()
                .map(CreateOrderItemCommand::productId)
                .toList();

        //order.applyDiscount(request.discountRate()); // 할인 적용
        //order.validate(); // 유효성 검사

        //Order 등록
        OrderEntity entity = OrderMapper.toOrderEntity(order);
        List<OrderItemEntity> entities = OrderMapper.toOrderItemEntities(order);

        return productQueryService.getProductsByIds(productIds) // Mono<List<ProductEntity>>
                .flatMap(products -> {
                    // 1. Entity 생성
                    OrderEntity orderEntity = OrderMapper.toOrderEntity(order);
                    List<OrderItemEntity> orderItemEntities = OrderMapper.toOrderItemEntities(order, products, orderEntity);
                    orderEntity.setOrderItems(orderItemEntities); // 연관관계 설정

                    // 2. 저장 후 조회
                    return orderCommandService.createOrder(orderEntity, orderItemEntities)
                            .then(orderQueryService.getOrder(orderId));
                })
                .map(orderEntity -> new OrderCommandResponse(
                        orderEntity.getId(),
                        orderEntity.getMemberId(),
                        orderEntity.getTotalPrice(),
                        orderEntity.getStatus().name()
                ));
    }

}
