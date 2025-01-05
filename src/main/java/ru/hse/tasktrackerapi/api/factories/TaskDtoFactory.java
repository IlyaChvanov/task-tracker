package ru.hse.tasktrackerapi.api.factories;

import org.springframework.stereotype.Component;
import ru.hse.tasktrackerapi.api.dto.TaskDto;
import ru.hse.tasktrackerapi.store.TaskEntity;

@Component
public class TaskDtoFactory {
    public TaskDto makeTaskDto(TaskEntity entity) {
        return TaskDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .creationDate(entity.getCreationDate())
                .build();
    }
}
