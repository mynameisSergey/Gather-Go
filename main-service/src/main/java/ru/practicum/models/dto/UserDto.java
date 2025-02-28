package ru.practicum.models.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Модель объекта User Data Transfer Object
 * (Пользователь)
 */
@Value
@Builder
public class UserDto {
    @Email(message = "Email должен быть в правильном формате")
    String email;
    Long id;
    @NotBlank(message = "Имя не должно быть пустым")
    String name;
}