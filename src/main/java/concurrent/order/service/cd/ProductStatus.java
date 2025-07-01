package concurrent.order.service.cd;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStatus {

    CREATED("CREATE", "주문생성"),
    COMPLETED("COMPLETED", "주문완료"),
    CANCELED("CANCELED", "주문취소"),
    ;

    private final String code;
    private final String name;

}
