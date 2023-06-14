package com.example.sns.like.infrastructure;

import com.example.sns.common.infrastructure.redis.RedisPrefix;
import com.example.sns.common.infrastructure.redis.RedisService;
import com.example.sns.like.domain.Like;
import com.example.sns.like.domain.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.sns.common.infrastructure.redis.RedisConst.LIKE_EXPIRED_SECONDS;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {

    private final LikeJpaRepository likeRDBRepository;
    private final RedisTemplate<String, Like> likeRedisTemplate;
    private final RedisService redisService;

    @Override
    @Cacheable(value = "likePushed", key = "#memberId+ ':' + #postId", cacheManager = "cacheManager")
    public Optional<Like> findByMemberIdAndPostId(Long memberId, Long postId) {
        return likeRDBRepository.findByMemberIdAndPostId(memberId, postId);
    }

    @Override
    public boolean existsByMemberIdAndPostId(Long memberId, Long postId) {
        return isExistsInCache(memberId, postId) || isExistsInRDB(memberId, postId);
    }

    private boolean isExistsInCache(Long memberId, Long postId) {
        String key = redisService.makeKey(RedisPrefix.LIKE_PUSH, memberId, postId);
        return Optional.ofNullable(likeRedisTemplate.opsForValue().get(key))
                .isPresent();
    }

    private boolean isExistsInRDB(Long memberId, Long postId) {
        return likeRDBRepository.existsByMemberIdAndPostId(memberId, postId);
    }

    @Override
    @CacheEvict(value = "likePushed", key = "#like.memberId + ':' + #like.postId", cacheManager = "cacheManager")
    public void remove(Like like) {
        likeRDBRepository.delete(like);
    }

    @Override
    public void save(Like like) {
        String key = redisService.makeKey(RedisPrefix.LIKE_PUSH, like.getMemberId(), like.getPostId());
        likeRedisTemplate.opsForValue().set(key, like, LIKE_EXPIRED_SECONDS, TimeUnit.SECONDS);
    }
}
