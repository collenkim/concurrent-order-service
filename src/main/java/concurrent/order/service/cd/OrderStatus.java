package concurrent.order.service.cd;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    CREATED("CREATE", "주문생성"),
    COMPLETED("COMPLETED", "주문완료"),
    CANCELED("CANCELED", "주문취소"),
    ;

    private final String code;
    private final String name;

    public static String getName(String code){
        return Arrays.stream(OrderStatus.values())
                .filter(status -> status.code.equalsIgnoreCase(code))
                .map(OrderStatus::getName)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown order status code: " + code));
    }
}
