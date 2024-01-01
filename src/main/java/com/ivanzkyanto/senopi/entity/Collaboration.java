package com.ivanzkyanto.senopi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "collaborations")
@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Collaboration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "note_id", referencedColumnName = "id")
    private Note note;

}
