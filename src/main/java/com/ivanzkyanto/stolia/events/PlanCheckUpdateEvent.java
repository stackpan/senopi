package com.ivanzkyanto.stolia.events;

import com.ivanzkyanto.stolia.entity.Plan;
import lombok.Getter;

public class PlanCheckUpdateEvent extends AbstractPlanEvent {

    @Getter
    private boolean isChecked;

    public PlanCheckUpdateEvent(Plan plan) {
        super(plan);
        isChecked = plan.isChecked();
    }
}
