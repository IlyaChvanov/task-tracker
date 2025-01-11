package ru.hse.tasktrackerapi.api.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;
import ru.hse.tasktrackerapi.api.dto.TaskDto;
import ru.hse.tasktrackerapi.api.exceptions.NotFoundException;
import ru.hse.tasktrackerapi.api.factories.TaskDtoFactory;
import ru.hse.tasktrackerapi.repository.TaskRepository;
import ru.hse.tasktrackerapi.repository.TaskStateRepository;
import ru.hse.tasktrackerapi.store.TaskEntity;
import ru.hse.tasktrackerapi.store.TaskStateEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Transactional
public class TaskController {

    private final TaskRepository taskRepository;
    private final TaskStateRepository taskStateRepository;
    private final TaskDtoFactory taskDtoFactory;

    private final String GET_TASKS = "api/{task_state_id}/tasks";
    private final String CREATE_TASK = "api/{task_state_id}/tasks";
    private final String UPDATE_TASK = "api/tasks/{task_id}";
    private final String DELETE_TASK = "api/tasks/{task_id}";
    private final String MOVE_TASK = "api/{task_id}/tasks/position/change";

    @GetMapping(GET_TASKS)
    public List<TaskDto> getTasks(@PathVariable(name = "task_state_id") Long taskStateId) {

        TaskStateEntity taskState = taskStateRepository.findById(taskStateId)
                .orElseThrow(() -> new NotFoundException(String.format("Task state with id %s not found", taskStateId)));

        return taskState.getTasks()
                .stream()
                .map(taskDtoFactory::makeTaskDto)
                .collect(Collectors.toList());
    }

    @PostMapping(CREATE_TASK)
    public TaskDto createTask(@PathVariable(name = "task_state_id") Long taskStateId,
                              @RequestParam(name = "task_name") String taskName,
                              @RequestParam(name = "description", required = false) String description) throws BadRequestException {
        if (taskName.trim().isEmpty()) {
            throw new BadRequestException("Name cannot be empty");
        }

        TaskStateEntity taskState = taskStateRepository.findById(taskStateId)
                .orElseThrow(() -> new NotFoundException(String.format("Task state with id %s not found", taskStateId)));

        taskState.getTasks()
                .stream()
                .filter(task -> task.getName().equalsIgnoreCase(taskName))
                .findFirst()
                .ifPresent(task ->
                {
                    try {
                        throw new BadRequestException(String.format("Task with name %s already exists", taskName));
                    } catch (BadRequestException e) {
                        throw new RuntimeException(e);
                    }
                });

        final TaskEntity taskEntity = taskRepository.saveAndFlush(TaskEntity.builder()
                .name(taskName)
                .description(description)
                .taskState(taskState)
                .build()
        );

        return taskDtoFactory.makeTaskDto(taskEntity);
    }

    @PatchMapping(UPDATE_TASK)
    public TaskDto updateTask(@PathVariable(name = "task_id") Long taskId,
                              @RequestParam(name = "task_name", required = false) String taskName,
                              @RequestParam(name = "description", required = false) String description) throws BadRequestException {

        if (taskName.trim().isEmpty()) {
            throw new BadRequestException("Name cannot be empty");
        }

        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(String.format("Task with id %s not found", taskId)));

        task.setName(taskName);
        task.setDescription(description);

        return taskDtoFactory.makeTaskDto(taskRepository.saveAndFlush(task));

    }

    @DeleteMapping(DELETE_TASK)
    public boolean deleteTask(@PathVariable(name = "task_id") Long taskId) {
        taskRepository.deleteById(taskId);
        return true;
    }

    @PatchMapping(MOVE_TASK)
    public TaskDto moveTask(@PathVariable(name = "task_id") Long task_id,
                            @RequestParam(name = "task_state_id") Long task_state_id) {

        TaskStateEntity taskState = taskStateRepository.findById(task_state_id)
                .orElseThrow(() -> new NotFoundException(String.format("Task state with id %s not found", task_state_id)));

        TaskEntity task = taskRepository.findByIdAndTaskStateProjectId(task_id, taskState.getProject().getId())
                .orElseThrow(() -> new NotFoundException(String.format("Task with id %s not found", task_id)));

        task.setTaskState(taskState);

        return taskDtoFactory.makeTaskDto(taskRepository.saveAndFlush(task));
    }
}
