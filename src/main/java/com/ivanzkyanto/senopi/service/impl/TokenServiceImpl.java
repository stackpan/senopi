package com.ivanzkyanto.senopi.service.impl;

import com.ivanzkyanto.senopi.model.TokenPayload;
import com.ivanzkyanto.senopi.service.TokenService;
import com.ivanzkyanto.senopi.util.TokenManager;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final SecretKey ACCESS_TOKEN_KEY;

    private final SecretKey REFRESH_TOKEN_KEY;

    private final Long ACCESS_TOKEN_EXPIRATION = 5 * 60L; // 5 minutes

    private final Long REFRESH_TOKEN_EXPIRATION = 10 * 24 * 60 * 60L; // 10 days

    public TokenServiceImpl() {
        ACCESS_TOKEN_KEY = Jwts.SIG.HS256.key().build();
        REFRESH_TOKEN_KEY = Jwts.SIG.HS256.key().build();
    }

    @Override
    public String generateAccessToken(TokenPayload payload) {
        return TokenManager.generateToken(payload, ACCESS_TOKEN_KEY, ACCESS_TOKEN_EXPIRATION);
    }

    @Override
    public String generateRefreshToken(TokenPayload payload) {
        return TokenManager.generateToken(payload, REFRESH_TOKEN_KEY, REFRESH_TOKEN_EXPIRATION);
    }

    @Override
    public TokenPayload verifyAccessToken(String accessToken) {
        return TokenManager.verifyToken(accessToken, ACCESS_TOKEN_KEY, () -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Anda tidak berhak mengakses resource ini"));
    }

    @Override
    public TokenPayload verifyRefreshToken(String refreshToken) {
        return TokenManager.verifyToken(refreshToken, REFRESH_TOKEN_KEY, () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token tidak valid"));
    }
}
