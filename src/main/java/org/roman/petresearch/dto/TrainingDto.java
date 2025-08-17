package org.roman.petresearch.dto;

import jakarta.validation.constraints.NotBlank;
import org.roman.petresearch.entity.TrainingStatus;
import org.roman.petresearch.entity.TrainingType;

import java.time.Instant;

public record TrainingDto(
    Long id,
    @NotBlank(message = "Название тренировки обязательно")
    String name,
    String description,
    Instant startedAt,
    TrainingStatus status,
    TrainingType type
) {} 