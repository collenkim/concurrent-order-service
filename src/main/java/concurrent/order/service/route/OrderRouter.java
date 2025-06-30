package concurrent.order.service.route;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
@RequiredArgsConstructor
public class OrderRouter {

    private final OrderCommandHandler commandHandler;
    private final OrderQueryHandler queryHandler;

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions
                .route(POST("/orders").and(accept(MediaType.APPLICATION_JSON)), commandHandler::createOrder);
                //.andRoute(GET("/orders/{orderId}").and(accept(MediaType.APPLICATION_JSON)), queryHandler::getOrder)
                //.andRoute(DELETE("/orders/{orderId}"), commandHandler::cancelOrder);
    }

}
