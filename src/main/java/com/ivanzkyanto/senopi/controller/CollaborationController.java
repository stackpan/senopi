package com.ivanzkyanto.senopi.controller;

import com.ivanzkyanto.senopi.annotation.Authenticated;
import com.ivanzkyanto.senopi.entity.User;
import com.ivanzkyanto.senopi.model.request.CollaborationRequest;
import com.ivanzkyanto.senopi.model.response.ApiResponse;
import com.ivanzkyanto.senopi.service.CollaborationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CollaborationController {

    @NonNull
    private CollaborationService collaborationService;

    @PostMapping(path = "/collaborations", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Map<String, String>> add(@Authenticated User user, @RequestBody CollaborationRequest request) {
        String collaborationId = collaborationService.add(user, request);
        return ApiResponse.<Map<String, String>>builder()
                .status("success")
                .data(Map.of("collaborationId", collaborationId))
                .build();
    }

    @DeleteMapping(path = "/collaborations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Void> delete(@Authenticated User user, @RequestBody CollaborationRequest request) {
        collaborationService.delete(user, request);
        return ApiResponse.<Void>builder()
                .status("success")
                .message("Kolaborasi berhasil dihapus")
                .build();
    }

}
