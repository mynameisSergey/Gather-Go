package ru.practicum.services.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.ConflictDeleteException;
import ru.practicum.exceptions.ConflictNameCategoryException;
import ru.practicum.mappers.CategoryMapper;
import ru.practicum.models.Category;
import ru.practicum.models.Event;
import ru.practicum.models.dto.CategoryDto;
import ru.practicum.models.dto.NewCategoryDto;
import ru.practicum.repositories.CategoryRepository;
import ru.practicum.repositories.EventRepository;
import ru.practicum.services.CategoryAdminService;

import java.util.List;

/**
 * Класс CategoryAdminServiceImp для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryAdminServiceImp implements CategoryAdminService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.newCategoryDtoToCategory(newCategoryDto);
        log.info("Получен запрос на добавление категории с названием: {}", newCategoryDto.getName());
        checkNameCategory(category.getName());
        return CategoryMapper.categoryToCategoryDto(categoryRepository.save(category));

    }

    @Override
    @Transactional
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.get(id);
        if (!categoryDto.getName().equals(category.getName())) {
            checkNameCategory(categoryDto.getName());
            category.setName(categoryDto.getName());
        }
        log.info("Получен запрос на обновлении категории c id: {}", id);
        return CategoryMapper.categoryToCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.get(id);
        if (isRelatedEvent(category)) {
            throw new ConflictDeleteException("Существуют события, связанные с категорией " + category.getName());
        }
        log.info("Получен запрос на удаление категории c id: {}", id);
        categoryRepository.delete(category);
    }

    /**
     * Метод проверки категории на дубликат
     *
     * @param name Название категории
     */
    private void checkNameCategory(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new ConflictNameCategoryException("Имя категории " + name + " уже есть в базе");
        }
    }

    /**
     * Метод проверки Категории в базе по объекту
     *
     * @param category Искомый объект категории
     * @return Найдена или нет искомая категория (true/false)
     */
    private boolean isRelatedEvent(Category category) {
        List<Event> findEventByCategory = eventRepository.findEventByCategoryIs(category);
        return !findEventByCategory.isEmpty();
    }
}