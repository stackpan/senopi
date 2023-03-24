package com.ivanzkyanto.stolia.repository;

import com.ivanzkyanto.stolia.entity.PlanGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanGroupRepository extends JpaRepository<PlanGroup, String> {
}
