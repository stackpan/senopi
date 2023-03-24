package com.ivanzkyanto.stolia.repository;

import com.ivanzkyanto.stolia.entity.Plan;
import com.ivanzkyanto.stolia.entity.PlanGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, String> {

    Optional<List<Plan>> findAllByGroup(PlanGroup group);

    void queryPlanByCheckedTrue();

    Integer countByGroup(PlanGroup group);

}
