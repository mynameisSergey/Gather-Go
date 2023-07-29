package ru.practicum.models.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * Модель объекта Compilation Data Transfer Object
 * (Подборка событий)
 */
@Value
@Builder
public class CompilationDto {
    private List<EventShortDto> events;
    private Long id;
    private boolean pinned;
    private String title;
}