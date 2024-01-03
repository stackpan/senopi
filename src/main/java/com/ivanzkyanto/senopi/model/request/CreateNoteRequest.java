package com.ivanzkyanto.senopi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateNoteRequest(

        @NotBlank
        @Size(max = 100)
        @Pattern(regexp = "(?i)^(?!true$|false$|\\d+$).*$")
        String title,

        @NotEmpty
        List<@Size(max = 24) @Pattern(regexp = "(?i)^(?!true$|false$|\\d+$).*$") String> tags,

        @NotBlank
        @Pattern(regexp = "(?i)^(?!true$|false$|\\d+$).*$")
        String body

) implements Request {
}
