package github.io.truongbn.distributedcache.config;

import java.time.Duration;
import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Validated
@RequiredArgsConstructor(onConstructor_ = {@ConstructorBinding})
public class RedisConfig {
    private final Duration readTimeout;
    private final Duration connectionTimeout;
    private final int retry;
    @NotEmpty
    private final List<String> nodes;
    private final int maxTotalPool;
    private final int maxIdlePool;
    private final int minIdlePool;
    private final Duration maxWait;
    public <T> GenericObjectPoolConfig<T> getPoolConfig() {
        GenericObjectPoolConfig<T> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(maxTotalPool);
        config.setMaxIdle(maxIdlePool);
        config.setMinIdle(minIdlePool);
        config.setMaxWait(maxWait);
        return config;
    }
}
