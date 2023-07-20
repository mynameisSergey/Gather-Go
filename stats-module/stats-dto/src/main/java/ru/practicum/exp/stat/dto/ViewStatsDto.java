package ru.practicum.exp.stat.dto;

import lombok.*;

/**
 * Модель объекта ViewStats Data Transfer Object
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ViewStatsDto {

    String app;
    String uri;
    Long hits;
}