package com.ivanzkyanto.senopi.service.impl;

import com.ivanzkyanto.senopi.entity.User;
import com.ivanzkyanto.senopi.model.request.RegisterUserRequest;
import com.ivanzkyanto.senopi.model.response.UserResponse;
import com.ivanzkyanto.senopi.repository.UserRepository;
import com.ivanzkyanto.senopi.security.BCrypt;
import com.ivanzkyanto.senopi.service.UserService;
import com.ivanzkyanto.senopi.service.ValidationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService
{

    @NonNull
    private UserRepository userRepository;

    @NonNull
    private ValidationService validationService;

    private final ResponseStatusException USER_NOT_FOUND_EXCEPTION = new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan");

    @Override
    @Transactional
    public String register(RegisterUserRequest request) {
        validationService.validatePayload(request);

        User user = User.builder()
                .username(request.username())
                .build();

        if (userRepository.exists(Example.of(user))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gagal menambahkan user. Username sudah digunakan.");

        user.setPassword(BCrypt.hashpw(request.password(), BCrypt.gensalt()));
        user.setFullname(request.fullname());

        userRepository.save(user);

        return user.getId().toString();
    }

    @Override
    public List<UserResponse> search(String username) {
        List<User> users = userRepository.searchByUsernameLike(String.format("%%%s%%", username));

        return users.stream()
                .map(UserResponse::buildFrom)
                .toList();
    }

    @Override
    public UserResponse get(String userId) {
        validationService.validateUuid(userId, () -> USER_NOT_FOUND_EXCEPTION);

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> USER_NOT_FOUND_EXCEPTION);

        return UserResponse.buildFrom(user);
    }
}
