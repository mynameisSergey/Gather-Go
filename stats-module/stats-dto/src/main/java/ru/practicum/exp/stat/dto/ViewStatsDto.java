package ru.practicum.exp.stat.dto;

import lombok.*;

/**
 * Модель объекта ViewStats Data Transfer Object.
 *
 * @param 'app' название приложения
 * @param 'uri' URI ресурса
 * @param 'hits' количество обращений
 */
@Data
@AllArgsConstructor
public class ViewStatsDto {

    private String app;
    private String uri;
    private Long hits;
}