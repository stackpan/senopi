package com.ivanzkyanto.stolia.service;

import com.ivanzkyanto.stolia.entity.Plan;
import com.ivanzkyanto.stolia.entity.PlanGroup;

import java.util.List;

public interface PlanService {

    List<Plan> getAllByGroup(PlanGroup group);

    List<Plan> getAllByGroupId(String groupId);

    Plan getById(String id);

    Plan createNew(Plan plan);

    void modify(Plan newPlan);

    void setCheckPlan(Plan plan, boolean flag);

    void delete(Plan plan);

}
