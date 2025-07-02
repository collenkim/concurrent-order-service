package concurrent.order.service.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "orderExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 항상 유지할 스레드 수
        executor.setMaxPoolSize(50); // 동시 작업이 많아질 경우 생성할 수 있는 최대 스레드 수.
        executor.setQueueCapacity(100); // core 수 이상의 요청이 들어왔을 때 대기열로 넘길 수 있는 요청 수
        executor.setThreadNamePrefix("order-async-");
        executor.initialize();
        return executor;
    }

}
