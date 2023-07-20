package ru.practicum.models.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.*;

/**
 * Модель объекта NewEvent Data Transfer Object
 * (Данные для добавления нового события)
 */
@Value
@Builder
public class NewEventDto {
    @NotBlank(message = "Поле annotation должно быть заполнено")
    @Size(min = 20, max = 2000, message = "Минимальное кол-во символов для описания: 20. Максимальное: 2000")
    String annotation;
    @NotNull(message = "category не должно быть пустым")
    Long category;
    @NotBlank(message = "Поле description должно быть заполнено")
    @Size(min = 20, max = 7000, message = "Минимальное кол-во символов для описания: 20. Максимальное: 7000")
    String description;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", message = "Неправильный формат даты")
    String eventDate;
    LocationDto location;
    Boolean paid;
    @PositiveOrZero
    Integer participantLimit;
    Boolean requestModeration;
    @NotBlank(message = "Поле title должно быть заполнено")
    @Size(min = 3, max = 120, message = "Минимальное кол-во символов для описания: 3. Максимальное: 120")
    String title;
}