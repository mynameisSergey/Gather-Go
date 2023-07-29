package ru.practicum.services.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictRequestException;
import ru.practicum.mappers.RequestMapper;
import ru.practicum.models.Event;
import ru.practicum.models.Request;
import ru.practicum.models.User;
import ru.practicum.models.dto.ParticipationRequestDto;
import ru.practicum.models.enums.EventState;
import ru.practicum.models.enums.RequestStatus;
import ru.practicum.repositories.EventRepository;
import ru.practicum.repositories.RequestRepository;
import ru.practicum.repositories.UserRepository;
import ru.practicum.services.RequestPrivateService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Класс RequestPrivateServiceImp для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestPrivateServiceImp implements RequestPrivateService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final ProcessingEvents processingEvents;
    private final UserRepository userRepository;

    @Override
    public List<ParticipationRequestDto> get(Long id) {
        User user = userRepository.get(id);
        List<Request> requests = requestRepository.findAllByRequesterIs(user);
        log.info("Получен запрос на получение всех запросов пользователя с id:" + id);
        return requests.stream().map(RequestMapper::requestToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto create(Long userId, Long eventId, HttpServletRequest httpServletRequest) {
        User user = userRepository.get(userId);
        Event event = eventRepository.get(eventId);
        if (event.getState().equals(EventState.PUBLISHED)) {
            addEventConfirmRequestAndSetViews(event, httpServletRequest);
        } else {
            event.setViews(0L);
            event.setConfirmedRequests(0L);
        }
        checkEventState(event);
        checkEventOwner(user, event);
        checkParticipantLimit(event);
        checkEventUser(userId, eventId);
        Request request;
        if (event.getParticipantLimit() != 0 && event.isRequestModeration()) {
            request = Request.builder()
                    .created(LocalDateTime.now())
                    .event(event)
                    .requester(user)
                    .status(RequestStatus.PENDING)
                    .build();
        } else {
            request = Request.builder()
                    .created(LocalDateTime.now())
                    .event(event)
                    .requester(user)
                    .status(RequestStatus.CONFIRMED)
                    .build();
        }
        try {
            eventRepository.save(event);
            log.info("Получен запрос на добавление запроса от пользователя с id: {} для события id: {}", userId, eventId);
            return RequestMapper.requestToParticipationRequestDto(requestRepository.save(request));
        } catch (DataAccessException e) {
            throw new BadRequestException("Ошибка при работе с базой данных");
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Ошибка в формировании запроса");
        }
    }

    @Override
    @Transactional
    public ParticipationRequestDto update(Long userId, Long requestId, HttpServletRequest httpServletRequest) {
        User user = userRepository.get(userId);
        Request request = requestRepository.get(requestId);
        if (!request.getRequester().equals(user)) {
            throw new ConflictRequestException("Пользователь с id: " + userId
                    + "не подавал заявку с id: " + request.getId());
        }
        request.setStatus(RequestStatus.CANCELED);
        Event event = request.getEvent();
        if (event.getState().equals(EventState.PUBLISHED)) {
            addEventConfirmRequestAndSetViews(event, httpServletRequest);
        } else {
            event.setViews(0L);
            event.setConfirmedRequests(0L);
        }
        try {
            eventRepository.save(event);
            log.info("Получен запрос на обновление запроса с id: {} от пользователя с id: {}", requestId, userId);
            return RequestMapper.requestToParticipationRequestDto(requestRepository.save(request));
        } catch (DataAccessException e) {
            throw new BadRequestException("Ошибка при работе с базой данных");
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Ошибка в формировании запроса");
        }
    }

    /**
     * Метод проверки пользователя на участие в события
     *
     * @param userId  ID пользователя
     * @param eventId ID события
     */
    private void checkEventUser(Long userId, Long eventId) {
        Optional<Request> request = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        if (request.isPresent()) {
            throw new ConflictRequestException("Пользователь с id: " + userId + "участник события с id: " + eventId);
        }
    }

    /**
     * Метод проверки пользователя на участие в своём событии
     *
     * @param user  Объект пользователя
     * @param event Объект события
     */
    private void checkEventOwner(User user, Event event) {
        if (Objects.equals(user.getId(), event.getInitiator().getId())) {
            throw new ConflictRequestException("Пользователь с id: " + user.getId()
                    + "владелец события с id: " + event.getId() + " и не может подавать заявку на участие");
        }
    }

    /**
     * Метод проверки статуса публикации события
     *
     * @param event Объект события
     */
    private void checkEventState(Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictRequestException("Событие с id: " + event.getId()
                    + " не опубликовано, нельзя подавать запросы на участие");
        }
    }

    /**
     * Метод проверки лимита заявок на участие в событии
     *
     * @param event Объект события
     */
    private void checkParticipantLimit(Event event) {
        if (event.getParticipantLimit() == event.getConfirmedRequests() && event.getParticipantLimit() != 0) {
            throw new ConflictRequestException("Событие с id: " + event.getId()
                    + " нельзя подавать запросы на участие, превышен лимит заявок");
        }
    }

    /**
     * Метод добавления подтверждённых событий
     *
     * @param event Объект события
     */
    private void addEventConfirmRequestAndSetViews(Event event, HttpServletRequest request) {
        long count = processingEvents.confirmedRequestsForOneEvent(event, RequestStatus.CONFIRMED);
        event.setConfirmedRequests(count);
        long views = processingEvents.searchViews(event, request);
        event.setViews(views);
    }
}