package github.io.truongbn.distributedcache.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import github.io.truongbn.distributedcache.config.RedisProperties;
import github.io.truongbn.distributedcache.model.FallbackDTO;
import github.io.truongbn.distributedcache.model.ItemDTO;
import github.io.truongbn.distributedcache.model.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class RedisController {
    private final RedisTemplate<String, byte[]> redisTemplate;
    private final ObjectMapper objectMapper;
    private final RedisProperties redisProperties;
    @GetMapping(path = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ItemType getItem(@PathVariable("itemId") String itemId) {
        try {
            byte[] cachedContent = redisTemplate.opsForValue().get(itemId);
            if (cachedContent != null) {
                return objectMapper.readValue(cachedContent, ItemDTO.class);
            }
        } catch (Exception e) {
            log.error("Error while getting item from cache", e);
        }
        // Fetch Data from the Database
        // Write Data into the Cache
        return new FallbackDTO("Cache miss;");
    }

    @PostMapping
    public void setItem(@RequestBody ItemDTO item) {
        try {
            byte[] serializedValue = objectMapper.writeValueAsBytes(item);
            redisTemplate.opsForValue().set(String.valueOf(item.getId()), serializedValue,
                    redisProperties.getTtl());
            // Store Data in the Database
        } catch (Exception e) {
            log.error("Error while writing to cache", e);
        }
    }
}
