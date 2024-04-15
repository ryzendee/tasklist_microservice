package com.app.taskservice.entity.task;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "userId must not be null")
    private Long userId;
    @NotNull(message = "title must not be null")
    private String title;
    @Lob
    private String description;
    private TaskStatus status;

    public TaskEntity(Long userId, String title, String description, TaskStatus status) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public TaskEntity(Long userId, String title, String description) {
        this.userId = userId;
        this.title = title;
        this.description = description;
    }
}
