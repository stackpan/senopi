package com.ivanzkyanto.senopi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivanzkyanto.senopi.annotation.Authenticated;
import com.ivanzkyanto.senopi.entity.User;
import com.ivanzkyanto.senopi.model.request.ExportNotesRequest;
import com.ivanzkyanto.senopi.model.response.ApiResponse;
import com.ivanzkyanto.senopi.service.ExportService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExportController {

    @NonNull
    private ExportService exportService;

    @PostMapping(path = "/export/notes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> exportNotes(@Authenticated User user, @RequestBody ExportNotesRequest request) throws JsonProcessingException {
        exportService.exportNotes(user, request);

        return ApiResponse.<Void>builder()
                .status("success")
                .message("Permintaan Anda dalam antrean")
                .build();
    }

}
