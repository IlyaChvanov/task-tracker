package ru.hse.tasktrackerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.tasktrackerapi.store.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
