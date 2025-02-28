package ru.practicum.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * Модель объекта Comment Data Transfer Object
 * (Комментарий)
 */
@Value
@Builder
public class CommentDto {
    Long id;
    String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;
    UserShortDto author;
    EventCommentDto event;
    String state;
}