package concurrent.order.service.application.facade;

import concurrent.order.service.application.aspect.DistributedLock;
import concurrent.order.service.application.command.dto.CreateOrderDto;
import concurrent.order.service.application.command.dto.CreateOrderItemDto;
import concurrent.order.service.application.command.service.ProductCommandService;
import concurrent.order.service.application.mapper.OrderItemMapper;
import concurrent.order.service.application.mapper.OrderMapper;
import concurrent.order.service.application.command.service.OrderCommandService;
import concurrent.order.service.application.query.OrderQueryService;
import concurrent.order.service.application.query.ProductQueryService;
import concurrent.order.service.application.query.dto.OrderResponseDto;
import concurrent.order.service.domain.model.Order;
import concurrent.order.service.domain.model.OrderItem;
import concurrent.order.service.exception.CompletedOrderCancelException;
import concurrent.order.service.exception.NotFoundProductException;
import concurrent.order.service.generator.IdGenerator;
import concurrent.order.service.infrastructure.rds.entity.OrderEntity;
import concurrent.order.service.infrastructure.rds.entity.OrderItemEntity;
import concurrent.order.service.infrastructure.rds.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;
    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;

    @Transactional
    @DistributedLock(key = "'lock:order:create:' + #request.userId", waitTime = 10, leaseTime = 30)
    public OrderResponseDto createOrderWithLock(CreateOrderDto request) {

        final String orderId = IdGenerator.getGenerateOrderId();
        Order order = OrderMapper.toDomain(orderId, request);
        order.validate();

        for(OrderItem orderItem : order.getItems()){
            orderItem.validate();
        }

        order.calculateDiscountAmount(); // 할인 적용

        List<String> productIds = request.items().stream()
            .map(CreateOrderItemDto::productId)
            .toList();

        List<ProductEntity> products = productQueryService.getProductsByIds(productIds);

        if (CollectionUtils.isEmpty(products)) {
            throw new NotFoundProductException("상품이 존재하지 않습니다.");
        }

        OrderEntity orderEntity = OrderMapper.toEntity(order);
        List<OrderItemEntity> orderItems = OrderItemMapper.toEntities(order, orderEntity, products);

        orderItems.forEach(item -> item.setOrder(orderEntity));
        orderEntity.addOrderItem(orderItems);

        orderCommandService.createOrder(orderEntity);

        //주문 재고 차감 (이벤트를 발행하여 상품 서비스 혹은 재고 관리 서비스에서 재고가 차감될 수 있도록 하는게 좋음)
        productCommandService.decreaseProductStockByOrderItems(orderItems);

        //OrderHistDocument document = OrderHistMapper.toDocument(orderEntity);
        //mongoTemplate.save(document);

        return OrderMapper.toResponseDto(orderQueryService.getOrderWithItemsAndProducts(orderId));
    }

    /**
     * 주문 정보 조회
     *
     * @param orderId
     * @return
     */
    public OrderResponseDto getOrderAsync(String orderId){
        return OrderMapper.toResponseDto(orderQueryService.getOrderWithItemsAndProducts(orderId));
    }

    /**
     * 주문 취소
     * @param orderId
     */
    @Transactional
    @DistributedLock(key = "'lock:order:cancel:' + #orderId")
    public void cancelOrderWithLock(String orderId) {
        OrderEntity order = orderQueryService.getOrder(orderId);

        if (order.isCancel()) {
            throw new CompletedOrderCancelException("이미 취소된 주문입니다. orderId=" + orderId);
        }

        orderCommandService.cancelOrder(order);
    }

}
