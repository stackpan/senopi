package com.ivanzkyanto.senopi.controller;

import com.ivanzkyanto.senopi.model.request.RegisterUserRequest;
import com.ivanzkyanto.senopi.model.response.ApiResponse;
import com.ivanzkyanto.senopi.model.response.UserResponse;
import com.ivanzkyanto.senopi.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    @NonNull
    private UserService userService;

    @PostMapping(path = "/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Map<String, String>> register(@RequestBody RegisterUserRequest request) {
        String userId = userService.register(request);
        return ApiResponse.<Map<String, String>>builder()
                .status("success")
                .message("User berhasil ditambahkan")
                .data(Map.of("userId", userId))
                .build();
    }

    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<Map<String, List<UserResponse>>> search(@RequestParam("username") String username) {
        List<UserResponse> matches = userService.search(username);
        return ApiResponse.<Map<String, List<UserResponse>>>builder()
                .status("success")
                .data(Map.of("users", matches))
                .build();
    }

    @GetMapping(path = "/users/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<Map<String, UserResponse>> get(@PathVariable("userId") String userId) {
        UserResponse user = userService.get(userId);
        return ApiResponse.<Map<String, UserResponse>>builder()
                .status("success")
                .data(Map.of("user", user))
                .build();
    }

}
