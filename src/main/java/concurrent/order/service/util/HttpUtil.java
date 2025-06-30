package concurrent.order.service.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.stereotype.Component;

public final class HttpUtil {

    private HttpUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * 호스트 이름에서 끝 4자리를 가져옵니다. (고유 suffix로 사용)
     * 예: "ip-172-31-27-15" → "2715"
     */
    public static String getHostSuffix() {
        try {
            String hostname = InetAddress.getLocalHost().getHostName(); // ex: ip-172-31-27-15
            String[] parts = hostname.split("-");
            if (parts.length >= 2) {
                StringBuilder sb = new StringBuilder();
                String last = parts[parts.length - 2] + parts[parts.length - 1]; // "27" + "15" = "2715"
                if (last.length() >= 4) {
                    return last.substring(last.length() - 4);
                } else {
                    return String.format("%04d", Integer.parseInt(last));
                }
            }
        } catch (UnknownHostException | NumberFormatException e) {
            // fallback 값
        }
        return "0000";
    }

}
