package ru.practicum.exp.stat.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Модель объекта Hit Data Transfer Object
 */
@Value
@Builder
public class HitDto {

    Long id;
    @NotBlank(message = "Поле \"app\" должно быть заполнено")
    String app;
    @NotBlank(message = "Поле \"uri\" должно быть заполнено")
    String uri;
    @NotBlank(message = "Поле \"ip\" должно быть заполнено")
    String ip;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", message = "Неправильный формат даты и времени")
    String timestamp;
}