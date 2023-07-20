package ru.practicum.services;


import ru.practicum.models.dto.CategoryDto;
import ru.practicum.models.dto.NewCategoryDto;

/**
 * Интерфейс CategoryAdminService для обработки логики запросов из CategoryAdminController
 */
public interface CategoryAdminService {

    /**
     * Метод добавления категории
     *
     * @param newCategoryDto Новая категория в объекте NewCategoryDto
     * @return добавленная CategoryDto
     */
    CategoryDto create(NewCategoryDto newCategoryDto);

    /**
     * Метод изменения категории
     *
     * @param id             ID категории
     * @param newCategoryDto категория в объекте NewCategoryDto
     * @return изменённая CategoryDto
     */
    CategoryDto update(Long id, CategoryDto newCategoryDto);

    /**
     * Метод  удаления категории
     *
     * @param id ID категории
     */
    void delete(Long id);
}