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
    List<EventShortDto> events;
    Long id;
    boolean pinned;
    String title;
}
