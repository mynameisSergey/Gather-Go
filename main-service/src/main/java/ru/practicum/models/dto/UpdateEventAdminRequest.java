package ru.practicum.models.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.models.enums.ActionState;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

/**
 * Модель объекта UpdateEventAdminRequest
 * (Данные для изменения информации о событии)
 */
@Value
@Builder
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000, message = "Минимальное кол-во символов для аннотации: 20. Максимальное: 2000")
    String annotation;
    @Positive(message = "ID категории должен быть положительным")
    Long category;
    @Size(min = 20, max = 7000, message = "Минимальное кол-во символов для описания: 20. Максимальное: 7000")
    String description;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", message = "Неправильный формат даты")
    String eventDate;
    LocationDto location;
    Boolean paid;
    @PositiveOrZero(message = "Лимит участников должен быть положительным или равен нулю")
    Integer participantLimit;
    Boolean requestModeration;
    ActionState stateAction;
    @Size(min = 3, max = 120, message = "Минимальное кол-во символов для заголовка: 5. Максимальное: 120")
    String title;
}