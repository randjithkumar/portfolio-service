package com.peplatform.portfolioservice.security.jwt;

import com.peplatform.portfolioservice.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private final Clock clock;

    public String generateAccessToken(UserDetails userDetails) {
        Instant now = clock.instant();
        Instant expiration = now.plusMillis(
                jwtProperties.accessTokenExpirationMs()
        );

        Map<String, Object> claims = Map.of(
                "authorities",
                userDetails.getAuthorities()
                        .stream()
                        .map(Object::toString)
                        .toList()
        );

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(signingKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ) {
        String username = extractUsername(token);

        return username.equalsIgnoreCase(userDetails.getUsername())
                && !isExpired(token);
    }

    public long getExpirationSeconds() {
        return jwtProperties.accessTokenExpirationMs() / 1000;
    }

    private boolean isExpired(String token) {
        Date expiration = extractClaim(
                token,
                Claims::getExpiration
        );

        return expiration.before(Date.from(clock.instant()));
    }

    private <T> T extractClaim(
            String token,
            Function<Claims, T> resolver
    ) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return resolver.apply(claims);
    }

    private SecretKey signingKey() {
        byte[] keyBytes = Decoders.BASE64.decode(
                jwtProperties.secret()
        );

        return Keys.hmacShaKeyFor(keyBytes);
    }
}