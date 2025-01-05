package ru.hse.tasktrackerapi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskStateDto {
    @NonNull
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private Long ordinal;

    @JsonProperty("creation_date")
    private Instant creationDate;


}
