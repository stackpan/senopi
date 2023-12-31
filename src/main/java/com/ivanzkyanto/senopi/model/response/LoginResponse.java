package com.ivanzkyanto.senopi.model.response;

import lombok.*;

@Value
@Builder
public class LoginResponse {

    String accessToken;

    String refreshToken;

}
