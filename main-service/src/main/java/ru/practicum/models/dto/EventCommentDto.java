package ru.practicum.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Модель объекта EventComment Data Transfer Object
 * (Комментарий к событию)
 */
@Value
@Builder
public class EventCommentDto {
    @NotBlank(message = "Аннотация не должна быть пустой")
    String annotation;
    @NotNull(message = "Категория не должна быть null")
    CategoryDto category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Дата события не должна быть null")
    LocalDateTime eventDate;
    Long id;
    UserShortDto initiator;
    @NotBlank(message = "Название не должно быть пустым")
    String title;
}