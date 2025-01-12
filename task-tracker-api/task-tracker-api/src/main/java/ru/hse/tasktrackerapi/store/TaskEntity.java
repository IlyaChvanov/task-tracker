package ru.hse.tasktrackerapi.store;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
@Builder
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    @Builder.Default
    private Instant creationDate = Instant.now();

    @ManyToOne
    @JoinColumn(name = "task_state_id", referencedColumnName = "id")
    private TaskStateEntity taskState;
}
