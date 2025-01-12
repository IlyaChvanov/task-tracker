package ru.hse.tasktrackerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.tasktrackerapi.store.TaskEntity;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Optional<TaskEntity> findByIdAndTaskStateProjectId(Long taskId, Long projectId);
}
