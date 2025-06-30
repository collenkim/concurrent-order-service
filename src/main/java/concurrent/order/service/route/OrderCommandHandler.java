package concurrent.order.service.route;

import concurrent.order.service.application.command.service.OrderCommandService;
import concurrent.order.service.application.command.dto.CreateOrderCommand;
import concurrent.order.service.application.facade.OrderFacade;
import jakarta.validation.Validator;
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

    private final OrderFacade facade;
    private final Validator validator;

    public Mono<ServerResponse> createOrder(ServerRequest req) {
        return req.bodyToMono(CreateOrderCommand.class)
            .doOnNext(command -> {
                var violations = validator.validate(command);
                if (!violations.isEmpty()) {
                    throw new IllegalArgumentException("Validation failed: " + violations);
                }
            })
            .flatMap(facade::createOrder) // ✅ Facade 호출
            .flatMap(response -> ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response));
    }

}
