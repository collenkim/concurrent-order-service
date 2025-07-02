package concurrent.order.service.application.facade;

import concurrent.order.service.application.aspect.DistributedLock;
import concurrent.order.service.application.command.dto.CreateOrderDto;
import concurrent.order.service.application.command.dto.CreateOrderItemDto;
import concurrent.order.service.application.mapper.OrderItemMapper;
import concurrent.order.service.application.mapper.OrderMapper;
import concurrent.order.service.application.command.service.OrderCommandService;
import concurrent.order.service.application.query.OrderQueryService;
import concurrent.order.service.application.query.ProductQueryService;
import concurrent.order.service.application.query.dto.OrderResponseDto;
import concurrent.order.service.domain.model.Order;
import concurrent.order.service.exception.CompletedOrderCancelException;
import concurrent.order.service.exception.NotFoundOrderException;
import concurrent.order.service.exception.NotFoundProductException;
import concurrent.order.service.generator.IdGenerator;
import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
import concurrent.order.service.infrastructure.rds.entity.ProductEntity;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;
    private final ProductQueryService productQueryService;

    /**
     * 주문 생성
     *  - 상품 정보 조회는 MSA 환겨에서라면 상품 서비스 조회 요청을 통해 데이터를 가져와야함
     *  - userId는 세션에서 가져오거나 인증 토큰 (JWT 등)에서 추출해야함
     *
     * @param request
     * @return
     */
    @Async("orderExecutor")
    public CompletableFuture<OrderResponseDto> createOrderAsyncWithLock(CreateOrderDto request) {
        return CompletableFuture.supplyAsync(() -> createOrderWithLockInternal(request));
    }

    @Transactional
    @DistributedLock(key = "'lock:order:create:' + #request.userId", waitTime = 10, leaseTime = 30)
    public OrderResponseDto createOrderWithLockInternal(CreateOrderDto request) {
        final String orderId = IdGenerator.getGenerateOrderId();
        Order order = OrderMapper.toDomain(orderId, request);

        //order.applyDiscount(request.discountRate()); // 할인 적용
        //order.validate(); // 유효성 검사

        List<String> productIds = request.items().stream()
            .map(CreateOrderItemDto::productId)
            .toList();

        List<ProductEntity> products = productQueryService.getProductsByIds(productIds);

        if (CollectionUtils.isEmpty(products)) {
            throw new NotFoundProductException("상품이 존재하지 않습니다.");
        }

        OrderEntity orderEntity = OrderMapper.toEntity(order);
        List<OrderItemEntity> orderItems = OrderItemMapper.toEntities(order, orderEntity, products);
        orderCommandService.createOrder(orderEntity, orderItems);

        //주문 재고 차감

        return OrderMapper.toResponseDto(orderQueryService.getOrderWithItemsAndProducts(orderId));
    }

    /**
     *
     * @param orderId
     * @return
     */
    @Async("orderExecutor")
    public CompletableFuture<OrderResponseDto> getOrderAsync(String orderId){
        return CompletableFuture.completedFuture(OrderMapper.toResponseDto(orderQueryService.getOrderWithItemsAndProducts(orderId)));
    }

    @Async("orderExecutor")
    public CompletableFuture<Void> cancelOrderAsyncWithLock(String orderId) {
        cancelOrderInternal(orderId);
        return CompletableFuture.completedFuture(null);
    }

    @Transactional
    @DistributedLock(key = "'lock:order:cancel:' + #orderId")
    public void cancelOrderInternal(String orderId) {
        OrderEntity order = orderQueryService.getOrder(orderId);

        if (order.isCancel()) {
            throw new CompletedOrderCancelException("이미 취소된 주문입니다. orderId=" + orderId);
        }

        orderCommandService.cancelOrder(order);
    }

}
