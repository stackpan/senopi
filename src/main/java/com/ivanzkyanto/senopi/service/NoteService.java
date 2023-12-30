package com.ivanzkyanto.senopi.service;

import com.ivanzkyanto.senopi.model.request.CreateNoteRequest;
import com.ivanzkyanto.senopi.model.request.UpdateNoteRequest;
import com.ivanzkyanto.senopi.model.response.NoteResponse;

import java.util.List;

public interface NoteService {

    String create(CreateNoteRequest request);

    List<NoteResponse> getAll();

    NoteResponse get(String noteId);

    void update(String noteId, UpdateNoteRequest request);

    void delete(String noteId);

}
