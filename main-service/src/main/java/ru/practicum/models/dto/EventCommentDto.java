package ru.practicum.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * Модель объекта EventComment Data Transfer Object
 * (Комментарий к событию)
 */
@Value
@Builder
public class EventCommentDto {
    String annotation;
    CategoryDto category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    Long id;
    UserShortDto initiator;
    String title;
}