package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.models.dto.CategoryDto;
import ru.practicum.models.dto.NewCategoryDto;
import ru.practicum.services.CategoryAdminService;

import javax.validation.Valid;

/**
 * Класс CategoryAdminController по энпоинту admin/categories
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryAdminService categoryAdminService;

    /**
     * Метод (эндпоинт) добавления категории
     *
     * @param newCategoryDto Новая категория в объекте NewCategoryDto
     * @return добавленная CategoryDto
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryAdminService.create(newCategoryDto);
    }

    /**
     * Метод (эндпоинт) изменения категории
     *
     * @param catId          ID категории
     * @param newCategoryDto категория в объекте NewCategoryDto
     * @return изменённая CategoryDto
     */
    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto update(@PathVariable Long catId, @Valid @RequestBody CategoryDto newCategoryDto) {
        return categoryAdminService.update(catId, newCategoryDto);
    }

    /**
     * Метод (эндпоинт) удаления категории
     *
     * @param catId ID категории
     */
    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long catId) {
        categoryAdminService.delete(catId);
    }

}