package com.example.sns.like.infrastructure;

import com.example.sns.common.infrastructure.redis.RedisPrefix;
import com.example.sns.common.infrastructure.redis.RedisService;
import com.example.sns.like.domain.Like;
import com.example.sns.like.domain.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.sns.common.infrastructure.redis.RedisConst.LIKE_EXPIRED_SECONDS;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisService redisService;

    @Override
    public void save(Like like) {
        String key = redisService.makeKey(RedisPrefix.LIKE_PUSH, like.getMemberId(), like.getPostId());
        redisTemplate.opsForValue().set(key, LocalDateTime.now().toString(), LIKE_EXPIRED_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public boolean existsByMemberIdAndPostId(Long memberId, Long postId) {
        return isExistsInCache(memberId, postId) || isExistsInDB(memberId, postId);
    }

    private boolean isExistsInCache(Long memberId, Long postId) {
        String key = redisService.makeKey(RedisPrefix.LIKE_PUSH, memberId, postId);
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .isPresent();
    }

    private boolean isExistsInDB(Long memberId, Long postId) {
        return likeJpaRepository.existsByMemberIdAndPostId(memberId, postId);
    }

    @Override
    public void removeByMemberIdAndPostId(Long memberId, Long postId) {
        String key = redisService.makeKey(RedisPrefix.LIKE_PUSH, memberId, postId);
        Boolean cacheDelete = redisTemplate.delete(key);
        if (isCacheDeleted(cacheDelete)) {
            likeJpaRepository.deleteByMemberIdAndPostId(memberId, postId);
        }
    }

    private boolean isCacheDeleted(Boolean cacheDelete) {
        return Boolean.FALSE.equals(cacheDelete);
    }

    @Override
    public int countByPostId(Long postId) {
        return likeJpaRepository.countByPostId(postId);
    }
}
