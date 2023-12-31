package com.ivanzkyanto.senopi.controller;

import com.ivanzkyanto.senopi.model.request.LoginRequest;
import com.ivanzkyanto.senopi.model.request.RefreshTokenRequest;
import com.ivanzkyanto.senopi.model.response.ApiResponse;
import com.ivanzkyanto.senopi.model.response.LoginResponse;
import com.ivanzkyanto.senopi.service.AuthService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    @NonNull
    AuthService authService;

    @PostMapping("/api/authentications")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ApiResponse.<LoginResponse>builder()
                .status("success")
                .message("Authentication berhasil ditambahkan")
                .data(loginResponse)
                .build();
    }

    @PutMapping("/api/authentications")
    public ApiResponse<Map<String, String>> refresh(@RequestBody RefreshTokenRequest request) {
        String accessToken = authService.refresh(request);
        return ApiResponse.<Map<String, String>>builder()
                .status("success")
                .message("Access Token berhasil diperbarui")
                .data(Map.of("accessToken", accessToken))
                .build();
    }

    @DeleteMapping("/api/authentications")
    public ApiResponse<Void> logout(@RequestBody RefreshTokenRequest request) {
        authService.logout(request);
        return ApiResponse.<Void>builder()
                .status("success")
                .message("Refresh token berhasil dihapus")
                .build();
    }

}
