package concurrent.order.service.api;

import concurrent.order.service.application.command.dto.CreateOrderDto;
import concurrent.order.service.application.command.service.OrderCommandService;
import concurrent.order.service.application.facade.OrderFacade;
import concurrent.order.service.application.query.OrderQueryService;
import concurrent.order.service.application.query.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderApi {

    private final OrderFacade orderFacade;

    /**
     * 주문 등록 API
     *
     * @param dto
     * @return
     */
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody CreateOrderDto dto) {
        return ResponseEntity.ok(orderFacade.createOrder(dto));
    }

    /**
     * 주문 정보 조회 API
     *
     * @param orderId
     * @return
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderFacade.getOrder(orderId));
    }

    /**
     * 주문 취소 API
     *
     * @param orderId
     * @return
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable String orderId) {
        return ResponseEntity.noContent().build();
    }

}
