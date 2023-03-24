package com.ivanzkyanto.stolia.service;

import com.ivanzkyanto.stolia.entity.Plan;
import com.ivanzkyanto.stolia.entity.PlanGroup;
import com.ivanzkyanto.stolia.events.PlanCheckUpdateEvent;
import com.ivanzkyanto.stolia.events.PlanCreatedEvent;
import com.ivanzkyanto.stolia.events.PlanDestroyedEvent;
import com.ivanzkyanto.stolia.exception.PlanNotFoundException;
import com.ivanzkyanto.stolia.repository.PlanRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService, ApplicationEventPublisherAware {

    private ApplicationEventPublisher eventPublisher;

    @NonNull
    private PlanRepository repository;

    @Override
    public List<Plan> getAllByGroup(PlanGroup group) throws PlanNotFoundException {
        return repository.findAllByGroup(group).orElseThrow(PlanNotFoundException::new);
    }

    @Override
    public Plan getById(String id) throws PlanNotFoundException {
        return repository.findById(id).orElseThrow(PlanNotFoundException::new);
    }

    @Override
    public Plan createNew(Plan plan) {
        Plan result = repository.save(plan);

        eventPublisher.publishEvent(new PlanCreatedEvent(result));
        return result;
    }

    @Override
    public void modify(Plan newPlan) throws PlanNotFoundException {
        consumePlan(newPlan.getId(), plan -> {
            plan.setTodo(newPlan.getTodo());
            plan.setDescription(newPlan.getDescription());

            repository.save(plan);
        });
    }

    @Override
    public void setCheckPlan(Plan plan, boolean flag) throws PlanNotFoundException {
        consumePlan(plan.getId(), realPlan -> {
            realPlan.setChecked(flag);
            realPlan.setCheckedAt((flag ? LocalDateTime.now() : null));

            repository.save(realPlan);
            eventPublisher.publishEvent(new PlanCheckUpdateEvent(realPlan));
        });
    }

    @Override
    public void delete(Plan intendedPlan) throws PlanNotFoundException {
        consumePlan(intendedPlan.getId(), plan -> {
            repository.delete(plan);

            eventPublisher.publishEvent(new PlanDestroyedEvent(plan));
        });
    }

    private void consumePlan(String id, Consumer<Plan> planConsumer) {
        repository.findById(id)
                .ifPresentOrElse(planConsumer, PlanNotFoundException::new);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}