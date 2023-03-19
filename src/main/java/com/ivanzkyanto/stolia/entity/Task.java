package com.ivanzkyanto.stolia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "task")
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
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date changedAt;

    @Column(nullable = false, length = 45)
    private String todo;

    private String description;

    @Column(name = "is_checked", nullable = false, columnDefinition = "boolean default false")
    private Boolean checked = false;


}
