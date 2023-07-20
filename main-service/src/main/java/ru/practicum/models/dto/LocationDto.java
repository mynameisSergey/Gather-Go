package ru.practicum.models.dto;

import lombok.Builder;
import lombok.Value;

/**
 * Модель объекта Location Data Transfer Object
 * (Широта и долгота места проведения события)
 */
@Value
@Builder
public class LocationDto {
    float lat;
    float lon;
}