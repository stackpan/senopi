package com.ivanzkyanto.senopi.util;

import com.ivanzkyanto.senopi.model.TokenPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

@Component
public class TokenManager {

    public static String generateToken(TokenPayload payload, SecretKey secretKey, Long expiration) {
        return Jwts.builder()
                .signWith(secretKey)
                .id(UUID.randomUUID().toString())
                .subject(payload.userId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * expiration))
                .compact();
    }

    public static <X extends Throwable> TokenPayload verifyToken(String token, SecretKey secretKey, Supplier<? extends X> exceptionSupplier) throws X {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            return new TokenPayload(jws.getPayload().getSubject());
        } catch (JwtException jwtException) {
            if (!Objects.isNull(exceptionSupplier.get())) {
                throw exceptionSupplier.get();
            }

            throw jwtException;
        }
    }

}
