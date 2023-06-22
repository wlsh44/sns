package com.example.sns.like.infrastructure;

import com.example.sns.common.infrastructure.redis.RedisPrefix;
import com.example.sns.common.infrastructure.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.sns.common.infrastructure.redis.RedisConst.LIKE_EXPIRED_SECONDS;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeBatchJobService {

    private static final int PARAMETER_OFFSET = 1;
    private static final int MEMBER_ID_INDEX = 2;
    private static final int POST_ID_INDEX = 3;

    private final JdbcTemplate jdbcTemplate;
    private final RedisService redisService;
    private final RedisTemplate<String, Object> redisTemplate;

    public void updateRDB() {
        List<String> keys = findLikeKeys();

        String sql = "INSERT IGNORE INTO likes (member_id, post_id, created_at, last_modified_at) values (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                String key = keys.get(i);
                Timestamp createdAt = getCreatedAt(key);
                String[] memberIdAndPostId = key.split(":");

                ps.setLong(PARAMETER_OFFSET, Long.parseLong(memberIdAndPostId[MEMBER_ID_INDEX]));
                ps.setLong(PARAMETER_OFFSET + 1, Long.parseLong(memberIdAndPostId[POST_ID_INDEX]));
                ps.setTimestamp(PARAMETER_OFFSET + 2, createdAt);
                ps.setTimestamp(PARAMETER_OFFSET + 3, createdAt);
            }

            @Override
            public int getBatchSize() {
                return keys.size();
            }
        });
    }

    private Timestamp getCreatedAt(String key) {
        String createdAt = (String) redisTemplate.opsForValue().get(key);
        return Timestamp.valueOf(toLocalDateTime(createdAt));
    }

    private static LocalDateTime toLocalDateTime(String value) {
        return Optional.ofNullable(value)
                .map(LocalDateTime::parse)
                .orElseGet(LocalDateTime::now);
    }

    private List<String> findLikeKeys() {
        LocalDateTime findStartTime = LocalDateTime.now();

        List<String> keys = redisService.scanKeys(RedisPrefix.LIKE_PUSH);

        log.info("{}개 배치 작업 진행", keys.size());
        LocalDateTime findEndTime = LocalDateTime.now();
        if (Duration.between(findStartTime, findEndTime).getSeconds() >= LIKE_EXPIRED_SECONDS * 60) {
            log.warn("좋아요 데이터 유실 발생");
        }
        return keys;
    }
}
