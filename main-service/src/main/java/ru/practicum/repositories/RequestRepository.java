package ru.practicum.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exceptions.ResourceNotFoundException;
import ru.practicum.models.Event;
import ru.practicum.models.Request;
import ru.practicum.models.User;
import ru.practicum.models.enums.RequestStatus;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс RequestRepository для обработки запросов к БД
 */
public interface RequestRepository extends JpaRepository<Request, Long> {
    /**
     * Находит все запросы по событию.
     *
     * @param event событие
     * @return список запросов
     */
    List<Request> findAllByEvent(Event event);

    /**
     * Находит все запросы по списку идентификаторов.
     *
     * @param ids список идентификаторов запросов
     * @return список запросов
     */
    List<Request> findAllByIdIsIn(List<Long> ids);

    /**
     * Находит все запросы по инициатору.
     *
     * @param requester инициатор запроса
     * @return список запросов
     */
    List<Request> findAllByRequesterIs(User requester);

    /**
     * Находит запрос по идентификатору инициатора и события.
     *
     * @param userId  идентификатор инициатора
     * @param eventId идентификатор события
     * @return запрос, если найден
     */
    Optional<Request> findByRequesterIdAndEventId(Long userId, Long eventId);

    /**
     * Подсчитывает количество запросов по событию и статусу.
     *
     * @param event  событие
     * @param status статус запроса
     * @return количество запросов
     */
    long countRequestByEventAndStatus(Event event, RequestStatus status);

    /**
     * Находит все запросы по списку событий и статусу.
     *
     * @param events список событий
     * @param status статус запроса
     * @return список запросов
     */
    List<Request> findAllByEventInAndStatus(List<Event> events, RequestStatus status);

    /**
     * Получает запрос по его идентификатору или выбрасывает исключение, если он не найден.
     *
     * @param id идентификатор запроса
     * @return запрос
     */
    default Request get(long id) {
        return findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Запрос на участие c id:  " + id + " не существует"));
    }
}