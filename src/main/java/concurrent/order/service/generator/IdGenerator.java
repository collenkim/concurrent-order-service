package concurrent.order.service.generator;

import concurrent.order.service.util.HttpUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public final class IdGenerator {

    private static final String ORDER_PREFIX = "ORD";
    private static final String PRODUCT_PREFIX = "PRD";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");

    private IdGenerator() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * 주문 ID 생성: ORD_240630142359123_hostSuffix_random
     * 예: ORD_240630142359123_2715_481
     */
    public static String getGenerateOrderId() {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String hostSuffix = HttpUtil.getHostSuffix();
        int random = ThreadLocalRandom.current().nextInt(100, 1000); // 3자리 랜덤

        return String.format("%s_%s_%s_%03d", ORDER_PREFIX, timestamp, hostSuffix, random);
    }

    /**
     * 상품 ID 생성: PRD_240630142359123_hostSuffix_random
     * 예: PRD_240630142359123_2715_481
     */
    public static String getGenerateProductId(){
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String hostSuffix = HttpUtil.getHostSuffix();
        int random = ThreadLocalRandom.current().nextInt(100, 1000); // 3자리 랜덤

        return String.format("%s_%s_%s_%03d", PRODUCT_PREFIX, timestamp, hostSuffix, random);
    }

}
