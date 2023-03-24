package com.ivanzkyanto.stolia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "plans")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private String id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private PlanGroup group;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime checkedAt;

    @Column(nullable = false, length = 64)
    private String todo;

    private String description;

    @Column(name = "is_checked", nullable = false, columnDefinition = "boolean default false")
    private boolean checked;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        checked = false;
    }

}
