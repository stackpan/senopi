package com.ivanzkyanto.stolia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task_groups")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 45)
    private String name;

    private String description;

    @Column(nullable = false, columnDefinition = "int unsigned default 0")
    private Integer taskCount;

    @Column(nullable = false, columnDefinition = "int unsigned default 0")
    private Integer checkedTaskCount;

    @OneToMany(mappedBy = "group")
    @Singular
    @NonNull
    private List<Task> tasks = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        taskCount = 0;
        checkedTaskCount = 0;
    }

}
