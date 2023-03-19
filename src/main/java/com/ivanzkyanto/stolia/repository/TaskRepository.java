package com.ivanzkyanto.stolia.repository;

import com.ivanzkyanto.stolia.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
}
