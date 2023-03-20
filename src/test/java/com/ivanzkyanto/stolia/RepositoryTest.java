package com.ivanzkyanto.stolia;

import com.ivanzkyanto.stolia.entity.Task;
import com.ivanzkyanto.stolia.entity.TaskGroup;
import com.ivanzkyanto.stolia.repository.TaskGroupRepository;
import com.ivanzkyanto.stolia.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryTest {

    @Autowired
    private TaskGroupRepository taskGroupRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testAddNewGroup() {
        TaskGroup taskGroup = TaskGroup.builder()
                .name("Example Group 1")
                .description("Example group description")
                .build();

        var result = taskGroupRepository.save(taskGroup);
        Assertions.assertTrue(taskGroupRepository.existsById(result.getId()));
    }

    @Test
    void testAddNewTasks() {
        TaskGroup taskGroup = TaskGroup.builder()
                .name("Example Group 2")
                .description("Example group description")
                .build();

        var taskGroupResults = taskGroupRepository.save(taskGroup);

        List<Task> tasks = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            tasks.add(Task.builder()
                    .todo("Example Todo " + i)
                    .description("Example todo description")
                    .group(taskGroup)
                    .build());
        }

        var taskResults = taskRepository.saveAll(tasks);

        Assertions.assertTrue(taskGroupRepository.existsById(taskGroupResults.getId()));
        taskResults.forEach(task -> {
            Assertions.assertTrue(taskRepository.existsById(task.getId()));
            Assertions.assertEquals(task.getGroup(), taskGroup);
        });
    }
}
