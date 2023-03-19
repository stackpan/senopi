package com.ivanzkyanto.stolia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date changedAt;

    @Column(nullable = false, length = 45)
    private String name;

    private String description;

    @Column(nullable = false, columnDefinition = "int unsigned default 0")
    private Integer checkedTaskCount;

    @Column(nullable = false, columnDefinition = "int unsigned default 0")
    private Integer taskCount;

    @OneToMany(mappedBy = "group")
    @Singular
    private Set<Task> tasks = new HashSet<>();

}
