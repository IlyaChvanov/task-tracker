package ru.hse.tasktrackerapi.api.factories;

import org.springframework.stereotype.Component;
import ru.hse.tasktrackerapi.api.dto.TaskStateDto;
import ru.hse.tasktrackerapi.store.TaskStateEntity;

@Component
public class TaskStateDtoFactory {
    public TaskStateDto makeTaskStateDto(TaskStateEntity entity) {
        return TaskStateDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .ordinal(entity.getOrdinal())
                .creationDate(entity.getCreationDate())
                .build();
    }
}
