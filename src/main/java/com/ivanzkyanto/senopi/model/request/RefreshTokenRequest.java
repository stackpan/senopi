package com.ivanzkyanto.senopi.model.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank String refreshToken) implements Request {
}
