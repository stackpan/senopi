package com.ivanzkyanto.senopi.controller;

import com.ivanzkyanto.senopi.model.response.ApiResponse;
import com.ivanzkyanto.senopi.service.StorageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UploadController {

    @NonNull
    private StorageService storageService;

    @PostMapping("/upload/images")
    public ApiResponse<Map<String, String>> upload(@RequestParam("data") MultipartFile file) {
        String path = storageService.store(file);
        String uri = MvcUriComponentsBuilder.fromMethodName(UploadController.class, "serveFile", path)
                .toUriString();

        return ApiResponse.<Map<String, String>>builder().data(Map.of("fileLocation", uri)).build();
    }

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);

        if (file == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(file);
    }

}
