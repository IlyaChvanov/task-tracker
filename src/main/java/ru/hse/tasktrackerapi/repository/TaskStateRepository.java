package ru.hse.tasktrackerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.tasktrackerapi.store.TaskStateEntity;

public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {
}
