package com.example.sns.common.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisService {

    private static final int SCAN_COUNT = 10;
    private static final String KEY_FORMAT = ":%s";
    private static final String ALL = "*";

    public String makeKey(RedisPrefix prefix, Object ...args) {
        StringBuilder key = new StringBuilder(prefix.getPrefix());
        for (Object arg : args) {
            key.append(String.format(KEY_FORMAT, arg));
        }
        return key.toString();
    }

    public List<String> scanKeys(RedisPrefix prefix, RedisTemplate<String, ?> redisTemplate) {
        String pattern = makeKey(prefix, ALL);
        RedisConnection connection = getRedisConnection(redisTemplate);
        ScanOptions scanOptions = getScanOptions(pattern);

        Cursor<byte[]> cursor = connection.scan(scanOptions);

        return getKeys(cursor);
    }

    private RedisConnection getRedisConnection(RedisTemplate<String, ?> redisTemplate) {
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        assert connectionFactory != null;
        return connectionFactory.getConnection();
    }

    private static ScanOptions getScanOptions(String pattern) {
        return ScanOptions
                .scanOptions()
                .match(pattern)
                .count(SCAN_COUNT)
                .build();
    }

    private List<String> getKeys(Cursor<byte[]> cursor) {
        List<String> keys = new ArrayList<>();
        while (cursor.hasNext()) {
            keys.add(new String(cursor.next()));
        }
        return keys;
    }
}
