package ru.practicum.exp.stat.serv.services;

import ru.practicum.exp.stat.dto.ViewStatsDto;

import java.util.List;

/**
 * Интерфейс StatService для обработки логики запросов из StatsController
 */
public interface StatService {

    /**
     * Метод получения статистики по посещениям
     *
     * @param start  Дата и время начала диапазона за который нужно выгрузить статистику
     * @param end    Дата и время конца диапазона за который нужно выгрузить статистику
     * @param uris   Список uri для которых нужно выгрузить статистику
     * @param unique Нужно ли учитывать только уникальные посещения (только с уникальным ip)
     * @return Сформированный список статистики по посещениям
     */
    List<ViewStatsDto> get(String start, String end, List<String> uris, boolean unique);
}