package concurrent.order.service.route;

import concurrent.order.service.application.query.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderQueryHandler {

    private final OrderQueryService orderQueryService;

    /*public Mono<ServerResponse> getOrder(ServerRequest req) {
        return req.bodyToMono(CreateOrderCommand.class)
            .doOnNext(req -> validator.validate(req)) // 직접 검증하거나, WebFlux 설정으로 처리
            .flatMap(commandService::createOrder)
            .flatMap(order -> ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(OrderMapper.toResponse(order)));

    }*/

}
