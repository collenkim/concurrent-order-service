package concurrent.order.service.application.async;

import concurrent.order.service.application.command.dto.CreateOrderDto;
import concurrent.order.service.application.facade.OrderFacade;
import concurrent.order.service.application.query.dto.OrderResponseDto;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderAsyncService {

    private final OrderFacade orderFacade;
    private final Executor orderExecutor;

    /**
     * 주문 등록 비동기 처리
     *
     * @param request
     * @return
     */
    public CompletableFuture<OrderResponseDto> createOrderAsync(CreateOrderDto request) {
        return CompletableFuture.supplyAsync(() ->
            orderFacade.createOrderWithLock(request),
            orderExecutor
        );
    }

    /**
     * 주문 정보 조회 비동기 처리
     *
     * @param orderId
     * @return
     */
    public CompletableFuture<OrderResponseDto> getOrderAsync(String orderId) {
        return CompletableFuture.supplyAsync(() ->
            orderFacade.getOrderAsync(orderId),
            orderExecutor
        );
    }

    /**
     * 주문 취소 비동기 처리
     *
     * @param orderId
     * @return
     */
    public CompletableFuture<Void> cancelOrderAsync(String orderId) {
        return CompletableFuture.runAsync(() ->
            orderFacade.cancelOrderWithLock(orderId),
            orderExecutor
        );
    }

}
