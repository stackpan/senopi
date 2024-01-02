package com.ivanzkyanto.senopi.repository;

import com.ivanzkyanto.senopi.entity.Collaboration;
import com.ivanzkyanto.senopi.entity.Note;
import com.ivanzkyanto.senopi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CollaborationRepository extends JpaRepository<Collaboration, UUID> {

    Optional<Collaboration> getByUser_IdAndNote_Id(UUID userId, UUID noteId);

    boolean existsByUserAndNote(User user, Note note);

}
