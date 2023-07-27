package ru.practicum.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.exceptions.ResourceNotFoundException;
import ru.practicum.models.Compilation;

import java.util.List;

/**
 * Интерфейс CompilationRepository для обработки запросов к БД
 */
@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("SELECT c FROM Compilation c WHERE c.pinned = :pinned OR :pinned IS NULL")
    List<Compilation> findAllByPinnedIs(Boolean pinned, Pageable pageable);

    default Compilation get(long id) {
        return findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Подборка событий c id:  " + id + " не существует"));
    }
}