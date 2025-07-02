package concurrent.order.service.api;

import concurrent.order.service.application.async.OrderAsyncService;
import concurrent.order.service.application.command.dto.CreateOrderDto;
import concurrent.order.service.application.query.dto.OrderResponseDto;
import concurrent.order.service.util.DeferredResultUtil;
import java.util.concurrent.CompletionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderApi {

    private final OrderAsyncService orderAsyncService;

    /**
     * 주문 등록 API
     *
     * @param dto
     * @return
     */
    @PostMapping
    public DeferredResult<ResponseEntity<OrderResponseDto>> createOrder(@RequestBody CreateOrderDto dto) {
        return DeferredResultUtil.fromCompletableFuture(orderAsyncService.createOrderAsync(dto));
    }

    /**
     * 주문 정보 조회 API
     *
     * @param orderId
     * @return
     */
    @GetMapping("/{orderId}")
    public DeferredResult<ResponseEntity<OrderResponseDto>> getOrder(@PathVariable String orderId) {
        return DeferredResultUtil.fromCompletableFuture(orderAsyncService.getOrderAsync(orderId));
    }

    /**
     * 주문 취소 API
     *
     * @param orderId
     * @return
     */
    @DeleteMapping("/{orderId}")
    public DeferredResult<ResponseEntity<Object>> cancelOrder(@PathVariable String orderId) {
        return DeferredResultUtil.fromVoidFuture(orderAsyncService.cancelOrderAsync(orderId));
    }

}
