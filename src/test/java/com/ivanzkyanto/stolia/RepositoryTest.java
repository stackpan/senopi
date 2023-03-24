package com.ivanzkyanto.stolia;

import com.ivanzkyanto.stolia.entity.PlanGroup;
import com.ivanzkyanto.stolia.repository.PlanGroupRepository;
import com.ivanzkyanto.stolia.repository.PlanRepository;
import com.ivanzkyanto.stolia.util.Dummies;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Random;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryTest {

    @Autowired
    private PlanGroupRepository groupRepository;

    @Autowired
    private PlanRepository planRepository;

    @Test
    void testGetAllTaskByGroup() {
        var taskGroupResults = groupRepository.saveAll(Dummies.generateTaskGroups(3));

        for (PlanGroup group : taskGroupResults) {
            planRepository.saveAll(Dummies.generateTasks(group, 2 + new Random().nextInt(3)));
        }

        taskGroupResults.forEach(group -> planRepository.findAllByGroup(group)
                .ifPresent(tasks -> tasks.forEach(task -> Assertions.assertEquals(group, task.getGroup()))));
    }

}
