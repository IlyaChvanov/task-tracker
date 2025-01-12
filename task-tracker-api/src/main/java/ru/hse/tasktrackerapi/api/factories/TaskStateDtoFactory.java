package ru.hse.tasktrackerapi.api.factories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.tasktrackerapi.api.dto.TaskStateDto;
import ru.hse.tasktrackerapi.store.TaskStateEntity;

import java.util.Collections;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TaskStateDtoFactory {

    private final TaskDtoFactory taskDtoFactory;

    public TaskStateDto makeTaskStateDto(TaskStateEntity entity) {
        return TaskStateDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .creationDate(entity.getCreationDate())
                .leftTaskStateId(entity.getLeftTaskState().map(TaskStateEntity::getId).orElse(null))
                .rightTaskStateId(entity.getRightTaskState().map(TaskStateEntity::getId).orElse(null))
                .tasks(
                        entity.getTasks() != null
                                ? entity.getTasks().stream()
                                .map(taskDtoFactory::makeTaskDto)
                                .collect(Collectors.toList())
                                : Collections.emptyList()
                )
                .build();
    }
}
