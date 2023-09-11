package github.io.truongbn.distributedcache.compression;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CompressedRedisSerializer implements RedisSerializer<byte[]> {
    private final CompressionAlgorithm compressionAlgorithm;
    @Override
    public @Nullable byte[] serialize(@Nullable byte[] data) throws SerializationException {
        if (data == null) {
            return null;
        }
        log.debug("Serialized Data Length: {} ", data.length);
        return compressData(data);
    }

    @Override
    public byte[] deserialize(byte[] data) throws SerializationException {
        if (data == null) {
            return null;
        }
        log.debug("Decompressed Data Length: {} ", data.length);
        return decompressData(data);
    }

    private byte[] compressData(byte[] data) {
        return compressionAlgorithm.compress(data);
    }

    private byte[] decompressData(byte[] compressedData) {
        return compressionAlgorithm.decompress(compressedData);
    }
}
