package ru.practicum.models.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Модель объекта NewCategory Data Transfer Object
 * (Данные для добавления новой категории)
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCategoryDto {
    @Size(max = 50, message = "Максимальное кол-во символов для поля name: 50")
    @NotBlank(message = "Поле name не должно быть пустым")
    private String name;
}