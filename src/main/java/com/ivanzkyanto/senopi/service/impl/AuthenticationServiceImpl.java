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
import com.ivanzkyanto.senopi.service.AuthenticationService;
import com.ivanzkyanto.senopi.service.TokenService;
import com.ivanzkyanto.senopi.service.ValidationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @NonNull
    private UserRepository userRepository;

    @NonNull
    private ValidationService validationService;

    @NonNull
    private AuthenticationRepository authenticationRepository;

    @NonNull
    private TokenService tokenService;

    private final ResponseStatusException INVALID_CREDENTIALS_EXCEPTION = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Kredensial yang Anda berikan salah");

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

        String accessToken = tokenService.generateAccessToken(tokenPayload);
        String refreshToken = tokenService.generateRefreshToken(tokenPayload);

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
        TokenPayload tokenPayload = tokenService.verifyRefreshToken(request.refreshToken());

        return tokenService.generateAccessToken(tokenPayload);
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
