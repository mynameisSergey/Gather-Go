package ru.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.models.Category;

/**
 * Интерфейс CategoryRepository для обработки запросов к БД
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Boolean existsByName(String name);
}