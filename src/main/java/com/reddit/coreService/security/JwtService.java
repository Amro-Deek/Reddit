package com.reddit.coreService.security;

import com.reddit.coreService.exceptions.CoreException;
import com.reddit.coreService.exceptions.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;


@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);

        Object userIdClaim = claims.get("userId");

        if (userIdClaim == null) {
            throw new CoreException("Missing 'userId' claim in token", ErrorCode.VALIDATION_ERROR);
        }

        if (userIdClaim instanceof Number) {
            return ((Number) userIdClaim).longValue(); // Handles Integer, Long, etc.
        }

        if (userIdClaim instanceof String) {
            try {
                return Long.parseLong((String) userIdClaim);
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Invalid userId format in token", e);
            }
        }

        throw new CoreException("Unexpected 'userId' claim type: " + userIdClaim.getClass(), ErrorCode.VALIDATION_ERROR);
    }


    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
