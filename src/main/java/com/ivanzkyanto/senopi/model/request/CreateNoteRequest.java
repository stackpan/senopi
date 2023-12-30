package com.ivanzkyanto.senopi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateNoteRequest(

        @NotBlank @Size(max = 100)
        String title,

        @NotEmpty
        List<@Size(max = 24) String> tags,

        @NotBlank
        String body

) implements Request {
}
