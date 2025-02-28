package ru.practicum.models.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Модель объекта InputComment Data Transfer Object
 * (Входной комментарий)
 */
@Value
@Builder
public class InputCommentDto {
    @NotBlank(message = "Поле text должно быть заполнено")
    @Size(max = 7000, message = "Максимальное кол-во символов для комментария: 7000")
    String text;
    @NotNull(message = "Поле userId должно быть заполнено")
    Long userId;
    @NotNull(message = "Поле eventId должно быть заполнено")
    Long eventId;
}