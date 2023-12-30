package com.ivanzkyanto.senopi.controller;

import com.ivanzkyanto.senopi.model.request.CreateNoteRequest;
import com.ivanzkyanto.senopi.model.request.UpdateNoteRequest;
import com.ivanzkyanto.senopi.model.response.ApiResponse;
import com.ivanzkyanto.senopi.model.response.NoteResponse;
import com.ivanzkyanto.senopi.service.NoteService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class NoteController {

    @NonNull
    private NoteService noteService;

    @PostMapping("/api/notes")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Map<String, String>> create(@RequestBody CreateNoteRequest request) {
        String noteId = noteService.create(request);
        return ApiResponse.<Map<String, String>>builder()
                .status("success")
                .message("Catatan berhasil ditambahkan")
                .data(Map.of("noteId", noteId))
                .build();
    }

    @GetMapping("/api/notes")
    public ApiResponse<Map<String, List<NoteResponse>>> getAll() {
        List<NoteResponse> notes = noteService.getAll();
        return ApiResponse.<Map<String, List<NoteResponse>>>builder()
                .status("success")
                .data(Map.of("notes", notes))
                .build();
    }

    @GetMapping("/api/notes/{noteId}")
    public ApiResponse<Map<String, NoteResponse>> get(@PathVariable String noteId) {
        NoteResponse note = noteService.get(noteId);
        return ApiResponse.<Map<String, NoteResponse>>builder()
                .status("success")
                .data(Map.of("note", note))
                .build();
    }

    @PutMapping("/api/notes/{noteId}")
    public ApiResponse<Void> update(@PathVariable String noteId, @RequestBody UpdateNoteRequest request) {
        noteService.update(noteId, request);
        return ApiResponse.<Void>builder()
                .status("success")
                .message("Catatan berhasil diperbarui")
                .build();
    }

    @DeleteMapping("/api/notes/{noteId}")
    public ApiResponse<Void> delete(@PathVariable String noteId) {
        noteService.delete(noteId);
        return ApiResponse.<Void>builder()
                .status("success")
                .message("Catatan berhasil dihapus")
                .build();
    }

}
