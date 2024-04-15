package com.app.taskservice.repository;

import com.app.taskservice.entity.task.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Page<TaskEntity> findAllByUserId(Long userId, Pageable pageable);
}
