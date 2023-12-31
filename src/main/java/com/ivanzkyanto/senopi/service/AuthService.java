package com.ivanzkyanto.senopi.service;

import com.ivanzkyanto.senopi.model.request.LoginRequest;
import com.ivanzkyanto.senopi.model.request.RefreshTokenRequest;
import com.ivanzkyanto.senopi.model.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    String refresh(RefreshTokenRequest request);

    void logout(RefreshTokenRequest request);

}
