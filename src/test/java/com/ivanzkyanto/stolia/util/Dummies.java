package com.ivanzkyanto.stolia.util;

import com.ivanzkyanto.stolia.entity.Plan;
import com.ivanzkyanto.stolia.entity.PlanGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Dummies {

    public static List<PlanGroup> generateTaskGroups(int total) {
        List<PlanGroup> planGroups = new ArrayList<>(total);

        for (int i = 0; i < total; i++) {
            var taskGroup = PlanGroup.builder().id(UUID.randomUUID().toString()).build();
            var id = taskGroup.getId();

            taskGroup.setName(String.format("Example group for %s", id));
            taskGroup.setDescription(String.format("Example group description for %s", id));

            planGroups.add(taskGroup);
        }

        return planGroups;
    }

    public static List<Plan> generateTasks(PlanGroup group, int total) {
        List<Plan> plans = new ArrayList<>(total);

        for (int i = 0; i < total; i++) {
            var task = Plan.builder().id(UUID.randomUUID().toString()).build();
            var id = task.getId();

            task.setTodo(String.format("Example todo for %s", id));
            task.setDescription(String.format("Example todo description for %s in group %s", id, group.getId()));
            task.setGroup(group);

            plans.add(task);
        }

        return plans;
    }

}
