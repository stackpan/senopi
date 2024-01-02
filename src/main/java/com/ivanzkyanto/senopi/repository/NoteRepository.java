package com.ivanzkyanto.senopi.repository;

import com.ivanzkyanto.senopi.entity.Note;
import com.ivanzkyanto.senopi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.stream.Stream;

public interface NoteRepository extends JpaRepository<Note, UUID> {

    Stream<Note> findAllByOwner(User owner);

    Stream<Note> findAllByCollaborators_User(User user);

}
