package ru.practicum.exp.stat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

/**
 * Модель объекта ViewStats Data Transfer Object
 */
@Value
@Builder
@AllArgsConstructor
public class ViewStatsDto {

    @NotBlank(message = "Поле \"app\" должно быть заполнено")
    String app;
    @NotBlank(message = "Поле \"uri\" должно быть заполнено")
    String uri;
    Long hits;
}