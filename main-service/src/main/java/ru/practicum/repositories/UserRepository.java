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
    /**
     * Находит пользователей по списку идентификаторов с поддержкой постраничного вывода.
     *
     * @param ids      список идентификаторов пользователей
     * @param pageable параметры постраничного вывода
     * @return список пользователей
     */
    List<User> findByIdIn(List<Long> ids, Pageable pageable);

    /**
     * Получает пользователя по его идентификатору или выбрасывает исключение, если он не найден.
     *
     * @param id идентификатор пользователя
     * @return пользователь
     * @throws ResourceNotFoundException если пользователь не найден
     */
    default User get(long id) {
        return findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Пользователь c id: " + id + " не существует"));
    }
}