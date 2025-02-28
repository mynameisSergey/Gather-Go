package ru.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exceptions.ResourceNotFoundException;
import ru.practicum.models.Category;

/**
 * Интерфейс CategoryRepository для обработки запросов к БД
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Проверяет, существует ли категория с заданным именем.
     *
     * @param name имя категории
     * @return true, если категория существует, иначе false
     */
    Boolean existsByName(String name);

    /**
     * Получает категорию по её идентификатору.
     *
     * @param id идентификатор категории
     * @return категория с заданным идентификатором
     * @throws ResourceNotFoundException если категория не найдена
     */
    default Category get(long id) {
        return findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Категория c id:  " + id + " не существует"));
    }
}