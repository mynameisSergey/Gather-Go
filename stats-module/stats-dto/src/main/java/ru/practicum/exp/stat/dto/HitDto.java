package ru.practicum.exp.stat.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Модель объекта Hit Data Transfer Object
 */
@Value
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class HitDto {

    private Long id;
    @NotBlank(message = "Поле \"app\" должно быть заполнено")
    private String app;
    @NotBlank(message = "Поле \"uri\" должно быть заполнено")
    private String uri;
    @NotBlank(message = "Поле \"ip\" должно быть заполнено")
    private String ip;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", message = "Неправильный формат даты и времени")
    private String timestamp;
}