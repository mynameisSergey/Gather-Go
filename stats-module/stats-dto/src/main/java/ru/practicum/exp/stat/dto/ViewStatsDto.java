package ru.practicum.exp.stat.dto;

import lombok.*;

/**
 * Модель объекта ViewStats Data Transfer Object
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ViewStatsDto {

    private String app;
    private String uri;
    private Long hits;
}