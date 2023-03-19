package com.ivanzkyanto.stolia.repository;

import com.ivanzkyanto.stolia.entity.TaskGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskGroupRepository extends JpaRepository<TaskGroup, String> {
}
