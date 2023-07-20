package ru.practicum.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
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
    List<Request> findAllByEvent(Event event);

    List<Request> findAllByIdIsIn(List<Long> ids);

    List<Request> findAllByRequesterIs(User requester);

    Optional<Request> findByRequesterIdAndEventId(Long userId, Long eventId);

    long countRequestByEventAndStatus(Event event, RequestStatus status);

    List<Request> findAllByEventInAndStatus(List<Event> events, RequestStatus status);
}