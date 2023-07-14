package ru.practicum.exp.stat.serv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exp.stat.serv.models.Hit;

/**
 * Интерфейс HitRepository для обработки запросов к БД
 */
public interface HitRepository extends JpaRepository<Hit, Long> {
}