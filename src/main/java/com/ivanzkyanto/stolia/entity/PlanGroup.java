package com.ivanzkyanto.stolia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plan_groups")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 64)
    private String name;

    private String description;

    @Column(nullable = false, columnDefinition = "int unsigned default 0")
    private Integer planCount;

    @Column(nullable = false, columnDefinition = "int unsigned default 0")
    private Integer checkedPlanCount;

    @OneToMany(mappedBy = "group")
    @Singular
    @NonNull
    private List<Plan> plans = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        planCount = 0;
        checkedPlanCount = 0;
    }

    public void addPlanCount() {
        planCount++;
    }

    public void subtractPlanCount() {
        if (planCount > 0) planCount--;
    }

    public void addCheckedPlanCount() {
        if (checkedPlanCount <= planCount) checkedPlanCount++;
    }

    public void subtractCheckedPlanCount() {
        if (checkedPlanCount > 0) planCount--;
    }

}
