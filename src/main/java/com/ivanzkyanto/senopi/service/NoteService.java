package com.ivanzkyanto.senopi.service;

import com.ivanzkyanto.senopi.entity.User;
import com.ivanzkyanto.senopi.model.request.CreateNoteRequest;
import com.ivanzkyanto.senopi.model.request.UpdateNoteRequest;
import com.ivanzkyanto.senopi.model.response.NoteResponse;

import java.util.List;

public interface NoteService {

    String create(User user, CreateNoteRequest request);

    List<NoteResponse> getAll(User user);

    NoteResponse get(User user, String noteId);

    void update(User user, String noteId, UpdateNoteRequest request);

    void delete(User user, String noteId);

}
