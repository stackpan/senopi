package com.ivanzkyanto.senopi.repository;

import com.ivanzkyanto.senopi.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {

}
