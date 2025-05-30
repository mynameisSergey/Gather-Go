package ru.practicum.services.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.ResourceNotFoundException;
import ru.practicum.mappers.CategoryMapper;
import ru.practicum.models.Category;
import ru.practicum.models.dto.CategoryDto;
import ru.practicum.repositories.CategoryRepository;
import ru.practicum.services.CategoryPublicService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс CategoryPublicServiceImp для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryPublicServiceImp implements CategoryPublicService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> get(int from, int size) {
        if (from < 0 || size <= 0)
            throw new IllegalArgumentException("Параметры 'from' и 'size' должны быть положительными");

        log.info("Получен запрос на список всех категорий");
        return categoryRepository.findAll(PageRequest.of(from, size)).stream()
                .map(CategoryMapper::categoryToCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto get(Long id) {
        log.info("Получен запрос на поиск категории по id: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Категория с id " + id + " не найдена"));
        return CategoryMapper.categoryToCategoryDto(category);
    }
}