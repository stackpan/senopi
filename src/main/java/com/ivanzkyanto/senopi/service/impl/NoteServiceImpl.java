package com.ivanzkyanto.senopi.service.impl;

import com.ivanzkyanto.senopi.entity.Note;
import com.ivanzkyanto.senopi.entity.Tag;
import com.ivanzkyanto.senopi.entity.User;
import com.ivanzkyanto.senopi.model.request.CreateNoteRequest;
import com.ivanzkyanto.senopi.model.request.UpdateNoteRequest;
import com.ivanzkyanto.senopi.model.response.NoteResponse;
import com.ivanzkyanto.senopi.repository.NoteRepository;
import com.ivanzkyanto.senopi.repository.TagRepository;
import com.ivanzkyanto.senopi.service.NoteService;
import com.ivanzkyanto.senopi.service.ValidationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    @NonNull
    public ValidationService validationService;

    @NonNull
    public NoteRepository noteRepository;

    @NonNull
    public TagRepository tagRepository;

    private final ResponseStatusException NOTE_NOT_FOUND_EXCEPTION = new ResponseStatusException(HttpStatus.NOT_FOUND, "Catatan tidak ditemukan");
    private final ResponseStatusException NOTE_FORBIDDEN_EXCEPTION = new ResponseStatusException(HttpStatus.FORBIDDEN, "Anda tidak berhak mengakses resource ini");

    @Override
    @Transactional
    public String create(User user, CreateNoteRequest request) {
        validationService.validatePayload(request);

        Note note = Note.builder()
                .title(request.title())
                .body(request.body())
                .owner(user)
                .build();

        noteRepository.save(note);

        request.tags().forEach(body -> {
            Tag tag = Tag.builder()
                    .note(note)
                    .body(body)
                    .build();

            tagRepository.save(tag);
        });

        return note.getId().toString();
    }

    @Override
    public List<NoteResponse> getAll(User user) {
        List<Note> notes = noteRepository.findAllByOwner(user);

        return notes.stream().map(NoteResponse::buildFrom).toList();
    }

    @Override
    public NoteResponse get(User user, String noteId) {
        UUID uuid = validationService.validateUuid(noteId, () -> NOTE_NOT_FOUND_EXCEPTION);
        Note note = noteRepository.findById(uuid).orElseThrow(() -> NOTE_NOT_FOUND_EXCEPTION);
        if (!note.getOwner().getId().equals(user.getId())) throw NOTE_FORBIDDEN_EXCEPTION;

        return NoteResponse.buildFrom(note);
    }

    @Override
    @Transactional
    public void update(User user, String noteId, UpdateNoteRequest request) {
        UUID uuid = validationService.validateUuid(noteId, () -> NOTE_NOT_FOUND_EXCEPTION);
        validationService.validatePayload(request);

        Note note = noteRepository.findById(uuid).orElseThrow(() -> NOTE_NOT_FOUND_EXCEPTION);
        if (!note.getOwner().getId().equals(user.getId())) throw NOTE_FORBIDDEN_EXCEPTION;

        note.setTitle(request.title());
        note.setBody(request.body());

        noteRepository.save(note);

        if (!note.getMappedTags().equals(request.tags())) {
            note.setTags(null);
            note.setTags(Tag.map(request.tags(), note));
        }
    }

    @Override
    @Transactional
    public void delete(User user, String noteId) {
        UUID uuid = validationService.validateUuid(noteId, () -> NOTE_NOT_FOUND_EXCEPTION);
        Note note = noteRepository.findById(uuid).orElseThrow(() -> NOTE_NOT_FOUND_EXCEPTION);
        if (!note.getOwner().getId().equals(user.getId())) throw NOTE_FORBIDDEN_EXCEPTION;

        noteRepository.delete(note);
    }
}
