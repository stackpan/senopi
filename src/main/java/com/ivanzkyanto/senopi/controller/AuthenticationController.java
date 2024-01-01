package com.ivanzkyanto.senopi.controller;

import com.ivanzkyanto.senopi.model.request.LoginRequest;
import com.ivanzkyanto.senopi.model.request.RefreshTokenRequest;
import com.ivanzkyanto.senopi.model.response.ApiResponse;
import com.ivanzkyanto.senopi.model.response.LoginResponse;
import com.ivanzkyanto.senopi.service.AuthenticationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    @NonNull
    AuthenticationService authenticationService;

    @PostMapping(path = "/authentications", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = authenticationService.login(request);
        return ApiResponse.<LoginResponse>builder()
                .status("success")
                .message("Authentication berhasil ditambahkan")
                .data(loginResponse)
                .build();
    }

    @PutMapping(path = "/authentications", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<Map<String, String>> refresh(@RequestBody RefreshTokenRequest request) {
        String accessToken = authenticationService.refresh(request);
        return ApiResponse.<Map<String, String>>builder()
                .status("success")
                .message("Access Token berhasil diperbarui")
                .data(Map.of("accessToken", accessToken))
                .build();
    }

    @DeleteMapping(path = "/authentications", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<Void> logout(@RequestBody RefreshTokenRequest request) {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .status("success")
                .message("Refresh token berhasil dihapus")
                .build();
    }

}
