package github.io.truongbn.distributedcache.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import github.io.truongbn.distributedcache.compression.CompressionAlgorithm;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Validated
@SuppressWarnings("NullAway.Init")
@ConfigurationProperties(prefix = "cache.redis")
public class RedisProperties {
    @NotNull
    private Duration ttl;
    @NotNull
    @NestedConfigurationProperty
    private RedisConfig config;
    @NotNull
    private CompressionAlgorithm compressionAlgorithm;
}
