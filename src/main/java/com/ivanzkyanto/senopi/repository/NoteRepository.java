package com.ivanzkyanto.senopi.repository;

import com.ivanzkyanto.senopi.entity.Note;
import com.ivanzkyanto.senopi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, UUID> {

    List<Note> findAllByOwner(User owner);

}
