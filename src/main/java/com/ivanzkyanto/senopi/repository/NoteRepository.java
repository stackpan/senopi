package com.ivanzkyanto.senopi.repository;

import com.ivanzkyanto.senopi.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, UUID> {
}
