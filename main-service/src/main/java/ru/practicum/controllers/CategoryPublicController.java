package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.models.dto.CategoryDto;
import ru.practicum.services.CategoryPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Класс CategoryPublicController по энпоинту categories
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoryPublicController {

    private final CategoryPublicService categoryPublicService;

    /**
     * Метод (эндпоинт) получения списка категории
     *
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора
     * @param size количество категорий в наборе
     * @return Список категорий
     */
    @GetMapping()
    public List<CategoryDto> get(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                 @Positive @RequestParam(defaultValue = "10") Integer size) {
        return categoryPublicService.get(from, size);
    }

    /**
     * Метод (эндпоинт) получения информации о категории по её ID
     *
     * @param catId ID категории
     * @return Объект CategoryDto
     */
    @GetMapping("/{catId}")
    public CategoryDto get(@PathVariable Long catId) {
        return categoryPublicService.get(catId);
    }
}