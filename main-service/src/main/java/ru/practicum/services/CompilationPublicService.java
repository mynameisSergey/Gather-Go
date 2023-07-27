package ru.practicum.services;


import ru.practicum.models.dto.CompilationDto;

import java.util.List;

/**
 * Интерфейс CompilationPublicService для обработки логики запросов из CompilationPublicController
 */
public interface CompilationPublicService {

    /**
     * Метод получения списка подборок событий
     *
     * @param pinned Закреплена ли подборка на главной странице сайта
     * @param from   Количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size   Количество элементов в наборе
     * @return Список подборок событий
     */
    List<CompilationDto> get(Boolean pinned, int from, int size);

    /**
     * Метод получения подборки событий по ID
     *
     * @param id ID подборки событий
     * @return Искомая подборка событий
     */
    CompilationDto get(Long id);
}