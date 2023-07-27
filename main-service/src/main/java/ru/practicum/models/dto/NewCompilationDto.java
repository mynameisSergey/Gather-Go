package ru.practicum.models.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Модель объекта NewCompilation Data Transfer Object
 * (Данные для добавления новой подборки событий)
 */
@Value
@Builder
public class NewCompilationDto {
    private List<Long> events;
    private boolean pinned;
    @Size(max = 50, message = "Максимальное кол-во символов для описания: 50")
    @NotBlank(message = "title не может быть пустым")
    private String title;
}