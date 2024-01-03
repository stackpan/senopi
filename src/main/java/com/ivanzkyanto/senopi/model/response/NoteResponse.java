package com.ivanzkyanto.senopi.model.response;

import com.ivanzkyanto.senopi.entity.Note;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoteResponse {

    String id;

    String title;

    String body;

    List<String> tags;

    String username;

    public static NoteResponse buildFrom(Note note) {
        return new NoteResponse(note.getId().toString(), note.getTitle(), note.getBody(), note.getMappedTags(), note.getOwner().getUsername());
    }

}
