package com.ivanzkyanto.senopi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(

        @NotBlank
        @Size(max = 20)
        String username,

        @NotBlank
        @Size(max = 32)
        String password,

        @NotBlank
        @Size(max = 50)
        String fullname

) implements Request {
}
