package com.ivanzkyanto.stolia;

import com.ivanzkyanto.stolia.entity.Plan;
import com.ivanzkyanto.stolia.entity.PlanGroup;
import com.ivanzkyanto.stolia.exception.PlanGroupNotFoundException;
import com.ivanzkyanto.stolia.exception.PlanNotFoundException;
import com.ivanzkyanto.stolia.service.PlanGroupService;
import com.ivanzkyanto.stolia.service.PlanService;
import com.ivanzkyanto.stolia.util.Dummies;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySources({
        @TestPropertySource("classpath:/test.properties")
})
public class ServiceTest {

    @Autowired
    private PlanService planService;

    @Autowired
    private PlanGroupService groupService;

    @Test
    void testCreateGroup() {
        var result = groupService.createNew(Dummies.generateTaskGroups(1).get(0));

        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(PlanGroup.class, result);
    }

    @Test
    void testCreatePlan() {
        var group = groupService.createNew(Dummies.generateTaskGroups(1).get(0));
        var plan = planService.createNew(Dummies.generateTasks(group, 1).get(0));

        Assertions.assertNotNull(plan);
        Assertions.assertInstanceOf(Plan.class, plan);
        Assertions.assertTrue(plan.getGroup().getPlanCount() > 0);
    }

    @Test
    void testModifyGroup() {
        var group = groupService.createNew(Dummies.generateTaskGroups(1).get(0));

        group.setName("Updated Group Name");
        group.setDescription("Updated Group Description");
        group.setPlanCount(1000);
        groupService.modify(group);

        var updatedGroup = groupService.getById(group.getId());

        Assertions.assertEquals(updatedGroup.getName(), group.getName());
        Assertions.assertEquals(updatedGroup.getDescription(), group.getDescription());
        Assertions.assertNotSame(updatedGroup.getPlanCount(), group.getPlanCount());
    }

    @Test
    void testModifyPlan() {
        var group = groupService.createNew(Dummies.generateTaskGroups(1).get(0));
        var plan = planService.createNew(Dummies.generateTasks(group, 1).get(0));

        plan.setTodo("Updated Group Name");
        plan.setDescription("Updated Group Description");
        plan.setChecked(true);

        planService.modify(plan);
        var updatedPlan = planService.getById(plan.getId());

        Assertions.assertEquals(updatedPlan.getTodo(), plan.getTodo());
        Assertions.assertEquals(updatedPlan.getDescription(), plan.getDescription());
        Assertions.assertNotSame(updatedPlan.isChecked(), plan.isChecked());
    }

    @Test
    void testCheckAndUncheckPlan() {
        var group = groupService.createNew(Dummies.generateTaskGroups(1).get(0));
        var plan = planService.createNew(Dummies.generateTasks(group, 1).get(0));

        Assertions.assertFalse(plan.isChecked());
        planService.setCheckPlan(plan, true);

        plan = planService.getById(plan.getId());
        Assertions.assertTrue(plan.isChecked());
        Assertions.assertNotNull(plan.getCheckedAt());

        planService.setCheckPlan(plan, false);
        plan = planService.getById(plan.getId());
        Assertions.assertFalse(plan.isChecked());
        Assertions.assertNull(plan.getCheckedAt());
    }

    @Test
    void testDeletePlan() {
        var group = groupService.createNew(Dummies.generateTaskGroups(1).get(0));
        var plan = planService.createNew(Dummies.generateTasks(group, 1).get(0));

        planService.delete(plan);
        Assertions.assertThrows(PlanNotFoundException.class, () -> Assertions.assertNull(planService.getById(plan.getId())));
    }

    @Test
    void testDeleteGroup() {
        var group = groupService.createNew(Dummies.generateTaskGroups(1).get(0));

        groupService.delete(group);
        Assertions.assertThrows(PlanGroupNotFoundException.class, () -> Assertions.assertNotNull(groupService.getById(group.getId())));
    }
}
