package ru.practicum.models.dto;

import lombok.Builder;
import lombok.Value;

/**
 * Модель объекта UserShort Data Transfer Object
 * (Пользователь (краткая информация))
 */
@Value
@Builder
public class UserShortDto {
    Long id;
    String name;
}