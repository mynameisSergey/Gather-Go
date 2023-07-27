package ru.practicum.services;


import ru.practicum.models.dto.CategoryDto;

import java.util.List;

/**
 * Интерфейс CategoryPublicService для обработки логики запросов из CategoryPublicController
 */
public interface CategoryPublicService {

    /**
     * Метод получения списка категории
     *
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора
     * @param size количество категорий в наборе
     * @return Список категорий
     */
    List<CategoryDto> get(int from, int size);

    /**
     * Метод получения информации о категории по её ID
     *
     * @param catId ID категории
     * @return Объект CategoryDto
     */
    CategoryDto get(Long catId);
}