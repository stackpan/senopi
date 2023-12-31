package com.ivanzkyanto.senopi.service.impl;

import com.ivanzkyanto.senopi.entity.Authentication;
import com.ivanzkyanto.senopi.entity.User;
import com.ivanzkyanto.senopi.exception.InvalidRefreshTokenException;
import com.ivanzkyanto.senopi.model.TokenPayload;
import com.ivanzkyanto.senopi.model.request.LoginRequest;
import com.ivanzkyanto.senopi.model.request.RefreshTokenRequest;
import com.ivanzkyanto.senopi.model.response.LoginResponse;
import com.ivanzkyanto.senopi.repository.AuthenticationRepository;
import com.ivanzkyanto.senopi.repository.UserRepository;
import com.ivanzkyanto.senopi.security.BCrypt;
import com.ivanzkyanto.senopi.service.AuthService;
import com.ivanzkyanto.senopi.service.ValidationService;
import com.ivanzkyanto.senopi.util.TokenManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @NonNull
    private UserRepository userRepository;

    @NonNull
    private ValidationService validationService;

    @NonNull
    private AuthenticationRepository authenticationRepository;

    @NonNull
    private TokenManager tokenManager;

    private final ResponseStatusException INVALID_CREDENTIALS_EXCEPTION = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Kredensial yang anda berikan salah");

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        validationService.validatePayload(request);

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> INVALID_CREDENTIALS_EXCEPTION);

        if (!BCrypt.checkpw(request.password(), user.getPassword())) {
            throw INVALID_CREDENTIALS_EXCEPTION;
        }

        TokenPayload tokenPayload = new TokenPayload(user.getId().toString());

        String accessToken = tokenManager.generateAccessToken(tokenPayload);
        String refreshToken = tokenManager.generateRefreshToken(tokenPayload);

        Authentication authentication = new Authentication(refreshToken);
        authenticationRepository.save(authentication);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public String refresh(RefreshTokenRequest request) {
        validationService.validatePayload(request);

        if (!authenticationRepository.existsById(request.refreshToken())) throw new InvalidRefreshTokenException();
        TokenPayload tokenPayload = tokenManager.verifyRefreshToken(request.refreshToken());

        return tokenManager.generateAccessToken(tokenPayload);
    }

    @Override
    @Transactional
    public void logout(RefreshTokenRequest request) {
        validationService.validatePayload(request);

        Authentication authentication = authenticationRepository
                .findById(request.refreshToken()).orElseThrow(InvalidRefreshTokenException::new);
        authenticationRepository.delete(authentication);
    }
}
