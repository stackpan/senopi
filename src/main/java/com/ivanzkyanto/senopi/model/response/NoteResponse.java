package com.ivanzkyanto.senopi.model.response;

import com.ivanzkyanto.senopi.entity.Note;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoteResponse {

    String id;

    String title;

    String body;

    List<String> tags;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    public static NoteResponse buildFrom(Note note) {
        return new NoteResponse(note.getId().toString(), note.getTitle(), note.getBody(), note.getMappedTags(), note.getCreatedAt(), note.getUpdatedAt());
    }

}
