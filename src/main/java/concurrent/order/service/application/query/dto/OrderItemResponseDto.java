package concurrent.order.service.application.query.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class OrderItemResponseDto {

    private String productId;
    private Integer quantity;
    private BigDecimal unitPrice;

}
