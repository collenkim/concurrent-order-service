package concurrent.order.service.application.facade;

import concurrent.order.service.application.command.dto.OrderCommandRequest;
import concurrent.order.service.application.command.dto.OrderCommandResponse;
import concurrent.order.service.application.command.service.OrderCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderCommandService orderCommandService;

    public Mono<OrderCommandResponse> createOrder(OrderCommandRequest request) {
        return orderCommandService.createOrder(request)
                .map(order -> new OrderCommandResponse(
                        order.getId(),
                        order.getMemberId(),
                        order.getTotalPrice(),
                        order.getStatus().name()
                ));
    }

}
