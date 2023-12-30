package com.ivanzkyanto.senopi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tags")
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String body;

    @ManyToOne
    @JoinColumn(name = "notes_id", referencedColumnName = "id")
    private Note note;

    public static List<Tag> map(List<String> data, Note note) {
        return data.stream()
                .map(body -> Tag.builder().note(note).body(body).build())
                .toList();
    }

}
