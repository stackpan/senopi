package com.ivanzkyanto.senopi.service.impl;

import com.ivanzkyanto.senopi.entity.Collaboration;
import com.ivanzkyanto.senopi.entity.Note;
import com.ivanzkyanto.senopi.entity.User;
import com.ivanzkyanto.senopi.model.request.CollaborationRequest;
import com.ivanzkyanto.senopi.repository.CollaborationRepository;
import com.ivanzkyanto.senopi.repository.NoteRepository;
import com.ivanzkyanto.senopi.repository.UserRepository;
import com.ivanzkyanto.senopi.service.CollaborationService;
import com.ivanzkyanto.senopi.service.ValidationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollaborationServiceImpl implements CollaborationService {

    @NonNull
    private ValidationService validationService;

    @NonNull
    private NoteRepository noteRepository;

    @NonNull
    private UserRepository userRepository;

    @NonNull
    private CollaborationRepository collaborationRepository;

    @Override
    @Transactional
    public String add(User user, CollaborationRequest request) {
        validationService.validatePayload(request);
        validationService.validateUuid(request.userId());
        validationService.validateUuid(request.noteId());

        Note note = checkNoteOwner(user, request);

        // Check to make sure the collaborator is not the note owner
        if (request.userId().equals(note.getOwner().getId().toString()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gagal menambahkan karena user adalah pemilik catatan");

        if (!userRepository.existsById(UUID.fromString(request.userId())))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pengguna tidak ditemukan");

        Collaboration collaboration = Collaboration.builder()
                .user(User.builder().id(UUID.fromString(request.userId())).build())
                .note(Note.builder().id(UUID.fromString(request.noteId())).build())
                .build();

        collaborationRepository.save(collaboration);

        return collaboration.getId().toString();
    }

    @Override
    public void delete(User user, CollaborationRequest request) {
        validationService.validatePayload(request);
        validationService.validateUuid(request.userId());
        validationService.validateUuid(request.noteId());

        checkNoteOwner(user, request);

        Collaboration collaboration = collaborationRepository.getByUser_IdAndNote_Id(
                UUID.fromString(request.userId()),
                UUID.fromString(request.noteId())
        ).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kolaborasi tidak ditemukan"));

        collaborationRepository.delete(collaboration);
    }

    private Note checkNoteOwner(User user, CollaborationRequest request) {
        Note note = noteRepository.findById(UUID.fromString(request.noteId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Catatan tidak ditemukan"));
        if (!note.getOwner().getId().equals(user.getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Anda tidak berhak mengakses resource ini");
        return note;
    }
}
