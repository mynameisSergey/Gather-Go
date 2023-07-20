package ru.practicum.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.models.Category;
import ru.practicum.models.dto.CategoryDto;
import ru.practicum.models.dto.NewCategoryDto;

/**
 * Утилитарный класс CategoryMapper для преобразования Category / CategoryDto / newCategoryDto
 */
@UtilityClass
public class CategoryMapper {

    /**
     * Преобразование CategoryDto в Category
     *
     * @param categoryDto Объект CategoryDto
     * @return Преобразованный объект Category
     */
    public Category categoryDtoToCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    /**
     * Преобразование Category в CategoryDto
     *
     * @param category Объект Category
     * @return Преобразованный объект CategoryDto
     */
    public CategoryDto categoryToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    /**
     * Преобразование NewCategoryDto в Category
     *
     * @param newCategoryDto Объект NewCategoryDto
     * @return Преобразованный объект Category
     */
    public Category newCategoryDtoToCategory(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }
}