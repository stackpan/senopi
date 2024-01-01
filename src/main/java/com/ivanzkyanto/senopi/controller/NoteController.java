package com.ivanzkyanto.senopi.controller;

import com.ivanzkyanto.senopi.annotation.Authenticated;
import com.ivanzkyanto.senopi.entity.User;
import com.ivanzkyanto.senopi.model.request.CreateNoteRequest;
import com.ivanzkyanto.senopi.model.request.UpdateNoteRequest;
import com.ivanzkyanto.senopi.model.response.ApiResponse;
import com.ivanzkyanto.senopi.model.response.NoteResponse;
import com.ivanzkyanto.senopi.service.NoteService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NoteController {

    @NonNull
    private NoteService noteService;

    @PostMapping(path = "/notes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Map<String, String>> create(@Authenticated User user, @RequestBody CreateNoteRequest request) {
        String noteId = noteService.create(user, request);
        return ApiResponse.<Map<String, String>>builder()
                .status("success")
                .message("Catatan berhasil ditambahkan")
                .data(Map.of("noteId", noteId))
                .build();
    }

    @GetMapping(path = "/notes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<Map<String, List<NoteResponse>>> getAll(@Authenticated User user) {
        List<NoteResponse> notes = noteService.getAll(user);
        return ApiResponse.<Map<String, List<NoteResponse>>>builder()
                .status("success")
                .data(Map.of("notes", notes))
                .build();
    }

    @GetMapping(path = "/notes/{noteId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<Map<String, NoteResponse>> get(@Authenticated User user, @PathVariable String noteId) {
        NoteResponse note = noteService.get(user, noteId);
        return ApiResponse.<Map<String, NoteResponse>>builder()
                .status("success")
                .data(Map.of("note", note))
                .build();
    }

    @PutMapping(path = "/notes/{noteId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<Void> update(@Authenticated User user, @PathVariable String noteId, @RequestBody UpdateNoteRequest request) {
        noteService.update(user, noteId, request);
        return ApiResponse.<Void>builder()
                .status("success")
                .message("Catatan berhasil diperbarui")
                .build();
    }

    @DeleteMapping(path = "/notes/{noteId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<Void> delete(@Authenticated User user, @PathVariable String noteId) {
        noteService.delete(user, noteId);
        return ApiResponse.<Void>builder()
                .status("success")
                .message("Catatan berhasil dihapus")
                .build();
    }

}
