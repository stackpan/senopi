package com.ivanzkyanto.senopi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

public record CollaborationRequest(

        @NotBlank @Size(max = 100)
        String userId,

        @NotBlank @Size(max = 100)
        String noteId

) implements Request {
}
