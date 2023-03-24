package com.ivanzkyanto.stolia.events;

import com.ivanzkyanto.stolia.entity.Plan;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

abstract class AbstractPlanEvent extends ApplicationEvent {

    @Getter
    private Plan plan;

    public AbstractPlanEvent(Plan plan) {
        super(plan);
        this.plan = plan;
    }
}
