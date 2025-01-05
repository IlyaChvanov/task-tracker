package ru.hse.tasktrackerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.tasktrackerapi.store.ProjectEntity;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
}
