package ru.practicum.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exceptions.ResourceNotFoundException;
import ru.practicum.models.User;

import java.util.List;

/**
 * Интерфейс UserRepository для обработки запросов к БД
 */
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByIdIn(List<Long> ids, Pageable pageable);

    default User get(long id) {
        return findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Пользователь c id: " + id + " не существует"));
    }
}