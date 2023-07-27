package ru.practicum.models.dto;

import lombok.Builder;
import lombok.Value;

/**
 * Модель объекта User Data Transfer Object
 * (Пользователь)
 */
@Value
@Builder
public class UserDto {
    private String email;
    private Long id;
    private String name;
}