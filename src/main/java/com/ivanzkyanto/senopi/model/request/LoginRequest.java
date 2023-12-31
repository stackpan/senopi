package com.ivanzkyanto.senopi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank
        @Size(max = 20)
        String username,

        @NotBlank
        @Size(max = 32)
        String password

) implements Request {
}
