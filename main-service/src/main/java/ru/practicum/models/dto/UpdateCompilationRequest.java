package ru.practicum.models.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Модель объекта UpdateCompilationRequest
 * (Изменение информации о подборке событий)
 */
@Value
@Builder
public class UpdateCompilationRequest {
    @NotEmpty(message = "Список событий не должен быть пустым")
    List<Long> events;
    Boolean pinned;
    @Size(max = 50, message = "Максимальное кол-во символов для описания: 50")
    String title;
}