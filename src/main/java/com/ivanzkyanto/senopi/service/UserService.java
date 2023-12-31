package com.ivanzkyanto.senopi.service;

import com.ivanzkyanto.senopi.model.request.RegisterUserRequest;
import com.ivanzkyanto.senopi.model.response.UserResponse;

import java.util.List;

public interface UserService {

    String register(RegisterUserRequest request);

    List<UserResponse> search(String keyword);

    UserResponse get(String userId);

}
