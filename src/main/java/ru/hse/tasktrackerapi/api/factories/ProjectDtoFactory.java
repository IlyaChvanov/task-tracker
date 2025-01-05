package ru.hse.tasktrackerapi.api.factories;

import org.springframework.stereotype.Component;
import ru.hse.tasktrackerapi.api.dto.ProjectDto;
import ru.hse.tasktrackerapi.store.ProjectEntity;

@Component
public class ProjectDtoFactory {
    public ProjectDto makeProjectDto(ProjectEntity entity) {
        return ProjectDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .creationDate(entity.getCreationDate())
                .build();
    }
}
