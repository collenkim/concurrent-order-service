package concurrent.order.service.application.facade;

import concurrent.order.service.application.command.dto.CreateOrderCommand;
import concurrent.order.service.application.command.dto.OrderCommandResponse;
import concurrent.order.service.application.mapper.OrderMapper;
import concurrent.order.service.application.command.service.OrderCommandService;
import concurrent.order.service.application.query.OrderQueryService;
import concurrent.order.service.domain.model.Order;
import concurrent.order.service.generator.OrderIdGenerator;
import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    public Mono<OrderCommandResponse> createOrder(CreateOrderCommand request) {

        //Order Id 생성
        final String orderId = OrderIdGenerator.getGenerateOrderId();

        Order order = OrderMapper.toDomain(orderId, "test123", request);

        order.applyDiscount(request.discountRate()); // 할인 적용
        order.validate(); // 유효성 검사

        //Order 등록
        OrderEntity entity = OrderMapper.toEntity(order);

        return orderCommandService.create(entity)
            .flatMap(aved -> orderQueryService.getOrderByOrderId(orderId))
            .map(queried -> new OrderCommandResponse(
                queried.getId(),
                queried.getMemberId(),
                queried.getTotalPrice(),
                queried.getStatus().name()
            ));
    }

}
