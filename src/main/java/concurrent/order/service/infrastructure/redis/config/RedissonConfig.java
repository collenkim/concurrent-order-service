package concurrent.order.service.infrastructure.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.connection.DnsAddressResolverGroupFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.database}")
    private int redisDatabase;

    private static final String REDISSON_HOST_PREFIX = "redis://";

    /**
     * Redission config 설정
     *
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {

        Config config = new Config();

        config.useSingleServer()
                .setAddress(REDISSON_HOST_PREFIX + redisHost + ":" + redisPort)
                .setDatabase(redisDatabase);

        config.setAddressResolverGroupFactory(new DnsAddressResolverGroupFactory());// DNS 주소 해석을 위한 설정
        config.setMinCleanUpDelay(5000);// 최소 GC 지연 시간 설정 (5초)

        return Redisson.create(config);
    }

}
