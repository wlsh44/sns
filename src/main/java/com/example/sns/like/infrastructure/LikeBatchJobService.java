package com.example.sns.like.infrastructure;

import com.example.sns.common.infrastructure.redis.RedisPrefix;
import com.example.sns.common.infrastructure.redis.RedisService;
import com.example.sns.like.domain.Like;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.sns.common.infrastructure.redis.RedisConst.LIKE_EXPIRED_SECONDS;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeBatchJobService {

    public static final int MEMBER_ID_INDEX = 2;
    public static final int POST_ID_INDEX = 3;

    private final JdbcTemplate jdbcTemplate;
    private final RedisService redisService;
    private final RedisTemplate<String, Like> likeRedisTemplate;

    public void updateRDB() {
        List<String> keys = findLikeKeys();

        String sql = "INSERT IGNORE INTO likes (member_id, post_id) values (?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                String key = keys.get(i);
                String[] split = key.split(":");
                ps.setLong(1, Long.parseLong(split[MEMBER_ID_INDEX]));
                ps.setLong(2, Long.parseLong(split[POST_ID_INDEX]));
            }

            @Override
            public int getBatchSize() {
                return keys.size();
            }
        });
    }

    private List<String> findLikeKeys() {
        LocalDateTime findStartTime = LocalDateTime.now();

        List<String> keys = redisService.scanKeys(RedisPrefix.LIKE_PUSH, likeRedisTemplate);
        log.info("{}개 배치 작업 진행", keys.size());

        LocalDateTime findEndTime = LocalDateTime.now();
        if (Duration.between(findStartTime, findEndTime).getSeconds() >= LIKE_EXPIRED_SECONDS * 60) {
            log.warn("좋아요 데이터 유실 발생");
        }
        return keys;
    }
}
