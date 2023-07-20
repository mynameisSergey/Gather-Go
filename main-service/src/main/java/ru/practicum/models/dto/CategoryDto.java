package ru.practicum.models.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Модель объекта Category Data Transfer Object
 * (Категория)
 */
@Value
@Builder
public class CategoryDto {
    Long id;
    @Size(max = 50, message = "Максимальное кол-во символов для поля name: 50")
    @NotBlank(message = "Поле name не должно быть пустым")
    String name;

    public CategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}