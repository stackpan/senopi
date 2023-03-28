package com.ivanzkyanto.stolia.service.impl;

import com.ivanzkyanto.stolia.entity.PlanGroup;
import com.ivanzkyanto.stolia.events.PlanCheckUpdateEvent;
import com.ivanzkyanto.stolia.events.PlanCreatedEvent;
import com.ivanzkyanto.stolia.events.PlanDestroyedEvent;
import com.ivanzkyanto.stolia.exception.PlanGroupNotFoundException;
import com.ivanzkyanto.stolia.exception.PlanNotFoundException;
import com.ivanzkyanto.stolia.repository.PlanGroupRepository;
import com.ivanzkyanto.stolia.service.PlanGroupService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class PlanGroupServiceImpl implements PlanGroupService {

    @NonNull
    private PlanGroupRepository repository;

    @Override
    public List<PlanGroup> getAll() {
        return repository.findAll();
    }

    @Override
    public PlanGroup getById(String id) throws PlanGroupNotFoundException {
        return repository.findById(id).orElseThrow(PlanGroupNotFoundException::new);
    }

    @Override
    public PlanGroup createNew(PlanGroup group) {
        return repository.save(group);
    }

    @Override
    public void modify(PlanGroup newGroup) throws PlanGroupNotFoundException {
        consumeGroup(newGroup.getId(), group -> {
            group.setName(newGroup.getName());
            group.setDescription(newGroup.getDescription());

            repository.save(group);
        });
    }

    @Override
    public void delete(PlanGroup intendedGroup) throws PlanGroupNotFoundException {
        consumeGroup(intendedGroup.getId(), group -> repository.delete(group));
    }

    private void consumeGroup(String id, Consumer<PlanGroup> planGroupConsumer) {
        repository.findById(id)
                .ifPresentOrElse(planGroupConsumer, PlanNotFoundException::new);
    }

    @EventListener(classes = PlanCreatedEvent.class)
    public void onPlanCreatedEvent(PlanCreatedEvent event) {
        var group = event.getPlan().getGroup();
        group.addPlanCount();

        repository.save(group);
    }

    @EventListener(classes = PlanCheckUpdateEvent.class)
    public void onPlanCheckUpdateEvent(PlanCheckUpdateEvent event) {
        var plan = event.getPlan();
        var group = plan.getGroup();

        if (plan.isChecked()) group.addCheckedPlanCount();
        else group.subtractCheckedPlanCount();

        repository.save(group);
    }

    @EventListener(classes = PlanDestroyedEvent.class)
    public void onPlanDestroyedEvent(PlanDestroyedEvent event) {
        var group = event.getPlan().getGroup();
        group.subtractPlanCount();

        repository.save(group);
    }

}
