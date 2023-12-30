package com.ivanzkyanto.senopi.model.response;

import com.ivanzkyanto.senopi.entity.Note;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoteResponse {

    private String id;

    private String title;

    private String body;

    private List<String> tags;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static NoteResponse buildFrom(Note note) {
        return new NoteResponse(note.getId().toString(), note.getTitle(), note.getBody(), note.getMappedTags(), note.getCreatedAt(), note.getUpdatedAt());
    }

}
