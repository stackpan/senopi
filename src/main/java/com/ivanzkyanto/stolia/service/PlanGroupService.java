package com.ivanzkyanto.stolia.service;

import com.ivanzkyanto.stolia.entity.PlanGroup;

import java.util.List;

public interface PlanGroupService {

    List<PlanGroup> getAll();

    PlanGroup getById(String id);

    PlanGroup createNew(PlanGroup group);

    void modify(PlanGroup newGroup);

    void delete(PlanGroup group);
}