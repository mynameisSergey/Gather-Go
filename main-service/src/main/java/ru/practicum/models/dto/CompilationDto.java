package ru.practicum.models.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Модель объекта Compilation Data Transfer Object
 * (Подборка событий)
 */
@Value
@Builder
public class CompilationDto {
    @NotEmpty(message = "Список событий не должен быть пустым")
    List<EventShortDto> events;
    Long id;
    boolean pinned;
    @NotBlank(message = "Название подборки не должно быть пустым")
    String title;
}