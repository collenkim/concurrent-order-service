package concurrent.order.service.route;

import concurrent.order.service.application.command.service.OrderCommandService;
import concurrent.order.service.application.command.dto.CreateOrderCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OrderCommandHandler {

    private final OrderCommandService commandService;

    public Mono<ServerResponse> createOrder(ServerRequest req) {
        return req.bodyToMono(CreateOrderCommand.class)
                .doOnNext(req -> validator.validate(req)) // 직접 검증하거나, WebFlux 설정으로 처리
                .flatMap(commandService::createOrder)
                .doOnNext()
                .flatMap(order -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(OrderMapper.toResponse(order)));

    }

}
