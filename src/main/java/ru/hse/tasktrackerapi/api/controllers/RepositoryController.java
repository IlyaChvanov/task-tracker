package ru.hse.tasktrackerapi.api.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hse.tasktrackerapi.api.dto.ProjectDto;
import ru.hse.tasktrackerapi.api.exceptions.BadRequestException;
import ru.hse.tasktrackerapi.api.exceptions.NotFoundException;
import ru.hse.tasktrackerapi.api.factories.ProjectDtoFactory;
import ru.hse.tasktrackerapi.repository.ProjectRepository;
import ru.hse.tasktrackerapi.store.ProjectEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@Transactional
public class RepositoryController {
    private final ProjectRepository projectRepository;
    private final ProjectDtoFactory projectDtoFactory;

    private final String FETCH_PROJECTS = "/api/projects";
    private final String CREATE_PROJECT = "/api/projects";
    private final String EDIT_REPOSITORY = "/api/projects/{project_id}";
    private final String DELETE_REPOSITORY = "/api/projects/{project_id}";

    @PostMapping(CREATE_PROJECT)
    public ProjectDto createRepository(@RequestParam String name) {
        if (name.trim().isEmpty()) {
            throw new BadRequestException("Repository name is empty");
        }

        projectRepository.findByName(name).ifPresent(project -> {
            throw new BadRequestException("Project " + project.getName() + " exists");
        });

        ProjectEntity project = projectRepository.saveAndFlush(
                ProjectEntity.builder()
                        .name(name)
                        .build()
        );

        return projectDtoFactory.makeProjectDto(project);
    }

    @PatchMapping(EDIT_REPOSITORY)
    public ProjectDto editRepository(@PathVariable("project_id") Long projectId, @RequestParam String name) {
        if (name.trim().isEmpty()) {
            throw new BadRequestException("Repository name is empty");
        }

        ProjectEntity project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project with id " + projectId + " not found"));

        projectRepository.findByName(name)
                .filter(projectEntity -> !projectEntity.getId().equals(project.getId()))
                .ifPresent(projectEntity -> {
                    throw new BadRequestException("Project " + project.getName() + " exists");
                });

        project.setId(projectId);

        projectRepository.saveAndFlush(project);

        return projectDtoFactory.makeProjectDto(project);
    }

    @GetMapping(FETCH_PROJECTS)
    public List<ProjectDto> getAllProjects(
            @RequestParam(value = "prefix_name", required = false) Optional<String> prefixName) {

        prefixName = prefixName.filter(pref -> !pref.trim().isEmpty());

        Stream<ProjectEntity> projectEntityStream = prefixName
                .map(projectRepository::streamAllByNameStartsWith)
                .orElse(Stream.empty());


        return projectEntityStream
                .map(projectDtoFactory::makeProjectDto).collect(Collectors.toList());
    }

    @DeleteMapping(DELETE_REPOSITORY)
    public Boolean deleteRepository(@PathVariable("project_id") Long projectId) {
        if (projectRepository.existsById(projectId)) {
            projectRepository.deleteById(projectId);
            return true;
        }
        throw new NotFoundException("Project with id " + projectId + " not found");
    }
}
