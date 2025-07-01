package concurrent.order.service.cd;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStatus {

    SELLING("SELLING", "판매중"),
    STOPPED("STOPPED", "판매중지"),
    SOLD_OUT("SOLD_OUT", "판매완료"),
    ;

    private final String code;
    private final String name;

}
