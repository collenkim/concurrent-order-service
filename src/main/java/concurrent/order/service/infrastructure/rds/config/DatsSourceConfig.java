package concurrent.order.service.infrastructure.rds.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatsSourceConfig {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.hikari.minimum-idle}")
    private int minimumIdle;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int maximumPoolSize;

    @Value("${spring.datasource.hikari.idle-timeout}")
    private Long idleTimeout;

    @Value("${spring.datasource.hikari.max-lifetime}")
    private Long maxLifeTime;

    @Value("${spring.datasource.hikari.connection-timeout}")
    private Long connectionTimeout;

    @Value("${spring.datasource.hikari.connection-test-query}")
    private String connectionTestQuery;

    @Value("${spring.datasource.hikari.pool-name}")
    private String poolName;

    @Value("${spring.datasource.hikari.auto-commit}")
    private Boolean autoCommit;

    @Value("${spring.datasource.hikari.leak-detection-threshold}")
    private Long leakDetectionThresholdMs;

    @Bean
    @Primary
    public DataSource dataSource() {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);

        config.setMinimumIdle(minimumIdle);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifeTime);
        config.setConnectionTimeout(connectionTimeout);

        config.setConnectionTestQuery(connectionTestQuery);
        config.setPoolName(poolName);
        config.setAutoCommit(autoCommit);
        config.setLeakDetectionThreshold(leakDetectionThresholdMs);

        return new HikariDataSource(config);
    }

}
