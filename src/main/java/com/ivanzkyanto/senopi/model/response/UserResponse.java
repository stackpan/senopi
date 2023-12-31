package com.ivanzkyanto.senopi.model.response;

import com.ivanzkyanto.senopi.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {

    String id;

    String username;

    String fullname;

    public static UserResponse buildFrom(User user) {
        return new UserResponse(user.getId().toString(), user.getUsername(), user.getFullname());
    }

}
