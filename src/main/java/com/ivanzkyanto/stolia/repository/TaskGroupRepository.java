package com.ivanzkyanto.stolia.repository;

import com.ivanzkyanto.stolia.entity.TaskGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskGroupRepository extends JpaRepository<TaskGroup, String> {
}
