package com.ivanzkyanto.stolia.events;

import com.ivanzkyanto.stolia.entity.Plan;

public class PlanCreatedEvent extends AbstractPlanEvent {

    public PlanCreatedEvent(Plan plan) {
        super(plan);
    }
}
