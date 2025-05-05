package com.fisa.card.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKeyPlain;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] decoded = Decoders.BASE64.decode(secretKeyPlain);
        System.out.println("Decoded key: " + new String(decoded)); // 로그 확인용
        this.key = Keys.hmacShaKeyFor(decoded);
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new SecurityException("Invalid JWT Token", e);
        }
    }
}
