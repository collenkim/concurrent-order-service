package concurrent.order.service.generator;

import concurrent.order.service.util.HttpUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public final class OrderIdGenerator {

    private static final String PREFIX = "ORD";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");

    private OrderIdGenerator() {
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

        return String.format("%s_%s_%s_%03d", PREFIX, timestamp, hostSuffix, random);
    }
}
