package ru.hse.tasktrackerapi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {
    @NonNull
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    @JsonProperty("creation_date")
    private Instant creationDate;
}
