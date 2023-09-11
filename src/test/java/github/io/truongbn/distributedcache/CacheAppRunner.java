package github.io.truongbn.distributedcache;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import github.io.truongbn.distributedcache.config.LocalRedisInitializer;

@SpringBootTest
@ComponentScan(basePackages = "github.io.truongbn.distributedcache")
@ConfigurationPropertiesScan(basePackages = "github.io.truongbn.distributedcache")
class CacheAppRunner {
    public static void main(String[] args) {
        new SpringApplicationBuilder(CacheAppRunner.class).initializers(new LocalRedisInitializer())
                .run(args);
    }
}
