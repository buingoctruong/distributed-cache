package github.io.truongbn.distributedcache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import github.io.truongbn.distributedcache.compression.CompressedRedisSerializer;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;

@Configuration
public class RedisClientConfig {
    @Profile("!local")
    @Bean(destroyMethod = "shutdown")
    public ClientResources redisClientResources() {
        return DefaultClientResources.create();
    }

    @Bean
    public ClientOptions redisClientOptions() {
        return ClientOptions.builder()
                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                .autoReconnect(true).build();
    }

    @Bean
    public LettucePoolingClientConfiguration lettucePoolingClientConfiguration(
            ClientOptions redisClientOptions, ClientResources redisClientResources,
            RedisProperties redisProperties) {
        return LettucePoolingClientConfiguration.builder()
                .commandTimeout(redisProperties.getConfig().getReadTimeout())
                .poolConfig(redisProperties.getConfig().getPoolConfig())
                .clientOptions(redisClientOptions).clientResources(redisClientResources).build();
    }

    @Bean
    public RedisClusterConfiguration customRedisCluster(RedisProperties redisProperties) {
        RedisConfig newConfig = redisProperties.getConfig();
        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration(
                newConfig.getNodes());
        // Set up password here if necessary
        // clusterConfiguration.setPassword(...);
        return clusterConfiguration;
    }

    @Bean
    public RedisConnectionFactory lettuceConnectionFactory(
            RedisClusterConfiguration customRedisCluster,
            LettucePoolingClientConfiguration lettucePoolingClientConfiguration) {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(customRedisCluster,
                lettucePoolingClientConfiguration);
        factory.afterPropertiesSet();
        return factory;
    }

    /**
     * Caution! Do not use `new GenericJackson2JsonRedisSerializer(objectMapper)`!
     * RedisTemplate needs information about each serialized class, but standard objectMapper
     * doesn't provide this information.
     * Either use `new GenericJackson2JsonSerializer()` or manually serialize objects before saving
     * to Redis
     */
    @Bean
    public RedisTemplate<String, byte[]> redisTemplate(
            RedisConnectionFactory lettuceConnectionFactory, RedisProperties redisProperties) {
        RedisTemplate<String, byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(
                new CompressedRedisSerializer(redisProperties.getCompressionAlgorithm()));
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
