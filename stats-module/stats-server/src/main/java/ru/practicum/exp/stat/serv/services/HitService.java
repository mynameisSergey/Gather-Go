package ru.practicum.exp.stat.serv.services;

import ru.practicum.exp.stat.dto.HitDto;

/**
 * Интерфейс HitService для обработки логики запросов из HitController
 */
public interface HitService {

    /**
     * Метод создания запроса Hit
     *
     * @param hitDto Объект запроса hit
     */
    void create(HitDto hitDto);
}