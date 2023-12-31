package com.ivanzkyanto.senopi.util;

import com.ivanzkyanto.senopi.exception.InvalidRefreshTokenException;
import com.ivanzkyanto.senopi.model.TokenPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class TokenManager {

    private final SecretKey ACCESS_TOKEN_KEY;
    private final SecretKey REFRESH_TOKEN_KEY;

    private final Long ACCESS_TOKEN_EXPIRATION = 5 * 60L; // 5 minutes

    private final Long REFRESH_TOKEN_EXPIRATION = 10 * 24 * 60 * 60L; // 10 days

    public TokenManager() {
        ACCESS_TOKEN_KEY = Jwts.SIG.HS256.key().build();
        REFRESH_TOKEN_KEY = Jwts.SIG.HS256.key().build();
    }

    public String generateAccessToken(TokenPayload payload) {
        return Jwts.builder()
                .signWith(ACCESS_TOKEN_KEY)
                .id(UUID.randomUUID().toString())
                .subject(payload.userId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * ACCESS_TOKEN_EXPIRATION))
                .compact();
    }

    public String generateRefreshToken(TokenPayload payload) {
        return Jwts.builder()
                .signWith(REFRESH_TOKEN_KEY)
                .id(UUID.randomUUID().toString())
                .subject(payload.userId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * REFRESH_TOKEN_EXPIRATION))
                .compact();
    }

    public TokenPayload verifyRefreshToken(String refreshToken) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(REFRESH_TOKEN_KEY)
                    .build()
                    .parseSignedClaims(refreshToken);

            return new TokenPayload(jws.getPayload().getSubject());
        } catch (JwtException e) {
            throw new InvalidRefreshTokenException();
        }
    }

}
