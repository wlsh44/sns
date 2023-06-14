package com.example.sns.common.infrastructure.redis;

public enum RedisPrefix {
    LIKE_PUSH("likePushed:");

    private final String prefix;

    RedisPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}