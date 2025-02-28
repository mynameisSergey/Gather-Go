package ru.practicum.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Модель объекта EventFull Data Transfer Object
 */
@Value
@Builder
public class EventFullDto {
    @NotBlank(message = "Аннотация не должна быть пустой")
    String annotation;
    @NotNull(message = "Категория не должна быть null")
    CategoryDto category;
    Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;
    @NotBlank(message = "Описание не должно быть пустым")
    String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    Long id;
    @NotNull(message = "Инициатор не должен быть null")
    UserShortDto initiator;
    LocationDto location;
    boolean paid;
    int participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;
    boolean requestModeration;
    @NotBlank(message = "Состояние не должно быть пустым")
    String state;
    @NotBlank(message = "Название не должно быть пустым")
    String title;
    Long views;
}