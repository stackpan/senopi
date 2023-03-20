package com.ivanzkyanto.stolia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "taskS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private TaskGroup group;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime changedAt;

    @Column(nullable = false, length = 45)
    private String todo;

    private String description;

    @Column(name = "is_checked", nullable = false, columnDefinition = "boolean default false")
    private Boolean checked;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        changedAt = createdAt;
        checked = false;
    }

    @PreUpdate
    public void preUpdate() {
        changedAt = LocalDateTime.now();
    }

}
