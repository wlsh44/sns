package com.example.sns.auth.infrastructure;

import com.example.sns.auth.exception.ExpiredTokenException;
import com.example.sns.auth.exception.InvalidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key key;
    private final long expireMillSecond;
    private final String issuer;

    public JwtProvider(
            @Value("${jwt.token.secret-key}") String key,
            @Value("${jwt.token.expired}") long expireMillSecond,
            @Value("${jwt.token.issuer}") String issuer) {
        this.key = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        this.expireMillSecond = expireMillSecond;
        this.issuer = issuer;
    }

    public String createToken(Long userId) {
        Date now = new Date();
        long expired = now.getTime() + expireMillSecond;

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(new Date(expired))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long parseMemberId(String token) {
        try {
            JwtParser parser = Jwts.parserBuilder()
                    .requireIssuer(issuer)
                    .setSigningKey(key)
                    .build();

            return Long.parseLong(parser.parseClaimsJws(token)
                    .getBody()
                    .getSubject());
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }
}
