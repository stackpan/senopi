package com.ivanzkyanto.senopi.service;

import com.ivanzkyanto.senopi.model.TokenPayload;

public interface TokenService {

    String generateAccessToken(TokenPayload payload);

    String generateRefreshToken(TokenPayload payload);

    TokenPayload verifyAccessToken(String accessToken);

    TokenPayload verifyRefreshToken(String refreshToken);

}
