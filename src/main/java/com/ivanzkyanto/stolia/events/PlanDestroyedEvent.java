package com.ivanzkyanto.stolia.events;

import com.ivanzkyanto.stolia.entity.Plan;

public class PlanDestroyedEvent extends AbstractPlanEvent {
    public PlanDestroyedEvent(Plan plan) {
        super(plan);
    }
}
