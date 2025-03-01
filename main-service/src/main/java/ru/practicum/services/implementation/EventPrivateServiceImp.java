package ru.practicum.services.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictRequestException;
import ru.practicum.exceptions.ForbiddenEventException;
import ru.practicum.exceptions.ResourceNotFoundException;
import ru.practicum.mappers.EventMapper;
import ru.practicum.mappers.LocationMapper;
import ru.practicum.mappers.RequestMapper;
import ru.practicum.models.Category;
import ru.practicum.models.Event;
import ru.practicum.models.Request;
import ru.practicum.models.User;
import ru.practicum.models.dto.*;
import ru.practicum.models.enums.ActionState;
import ru.practicum.models.enums.EventState;
import ru.practicum.models.enums.RequestStatus;
import ru.practicum.repositories.CategoryRepository;
import ru.practicum.repositories.EventRepository;
import ru.practicum.repositories.RequestRepository;
import ru.practicum.repositories.UserRepository;
import ru.practicum.services.EventPrivateService;
import ru.practicum.util.DateFormatter;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Класс EventPrivateServiceImp для отработки логики запросов и логирования
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPrivateServiceImp implements EventPrivateService {

    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final ProcessingEvents processingEvents;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<EventShortDto> get(Long userId, int from, int size, HttpServletRequest request) {
        if (from < 0 || size <= 0)
            throw new BadRequestException("Параметры 'from' и 'size' должны быть положительными.");


        userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("Пользователь с id " + userId + " не найден.")
        );

        Pageable pageable = PageRequest.of(from, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);
        List<Event> eventsAddViews = processingEvents.addViewsInEventsList(events, request);
        List<Event> newEvents = processingEvents.confirmRequests(eventsAddViews);
        log.info("Получен приватный запрос на получение всех событий для пользователя с id: {}", userId);
        return newEvents.stream().map(EventMapper::eventToEventShortDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventFullDto getEvent(Long userId, Long eventId, HttpServletRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + userId + " не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Событие с id " + eventId + " не найдено"));

        checkOwnerEvent(event, user);
        addEventConfirmRequestAndSetViews(event, request);

        log.info("Получен приватный запрос на получение события с id: {} для пользователя с id: {}", eventId, userId);
        return EventMapper.eventToEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        checkEventDate(DateFormatter.formatDate(newEventDto.getEventDate()));

        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("Пользователь с id " + userId + " не найден.")
        );

        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> new ResourceNotFoundException("Категория с id " + newEventDto.getCategory() + " не найдена.")
        );

        Long views = 0L;
        Long confirmedRequests = 0L;

        Event event = EventMapper.newEventDtoToCreateEvent(newEventDto, user, category, views, confirmedRequests);
        log.info("Получен приватный запрос на добавление события пользователем с id: {}", userId);
        return EventMapper.eventToEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto get(Long userId, Long eventId, HttpServletRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("Пользователь с id " + userId + " не найден.")
        );

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ResourceNotFoundException("Событие с id " + eventId + " не найдено.")
        );
        checkOwnerEvent(event, user);
        addEventConfirmRequestAndSetViews(event, request);
        log.info("Получен приватный запрос на получение события с id: {} для пользователя с id: {}", eventId, userId);
        return EventMapper.eventToEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEvent, HttpServletRequest request) {
        if (updateEvent.getEventDate() != null) {
            checkEventDate(DateFormatter.formatDate(updateEvent.getEventDate()));
        }
        Event event = eventRepository.get(eventId);
        User user = userRepository.get(userId);
        checkOwnerEvent(event, user);
        eventAvailability(event);
        if (updateEvent.getAnnotation() != null && !updateEvent.getAnnotation().isBlank()) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoryRepository.get(updateEvent.getCategory());
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null && !updateEvent.getDescription().isBlank()) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(DateFormatter.formatDate(updateEvent.getEventDate()));
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(LocationMapper.locationDtoToLocation(updateEvent.getLocation()));
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getStateAction() != null) {
            event.setState(determiningTheStatusForEvent(updateEvent.getStateAction()));
        }
        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            event.setTitle(updateEvent.getTitle());
        }
        if (event.getState().equals(EventState.PUBLISHED)) {
            addEventConfirmRequestAndSetViews(event, request);
        } else {
            event.setViews(0L);
            event.setConfirmedRequests(0L);
        }
        log.info("Получен приватный запрос на обновление события с id: {} для пользователя с id: {}", eventId, userId);
        return EventMapper.eventToEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId, HttpServletRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + userId + " не найден"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Событие с id " + eventId + " не найдено"));
        checkOwnerEvent(event, user);
        List<Request> requests = requestRepository.findAllByEvent(event);
        log.info("Получен приватный запрос на получение всех запросов для события с id: {} для пользователя с id: {}", eventId, userId);
        return requests.stream().map(RequestMapper::requestToParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateStatus(Long userId, Long eventId,
                                                       EventRequestStatusUpdateRequest eventRequest,
                                                       HttpServletRequest request) {
        Event event = getEventOrThrow(eventId);
        User user = getUserOrThrow(userId);
        checkOwnerEvent(event, user);

        log.info("Получен приватный запрос на обновление статуса запроса для события с id: {} для пользователя с id: {}", eventId, userId);

        initializeEventViewsAndRequests(event, request);

        validateParticipantLimit(event, eventId);

        List<Request> requests = requestRepository.findAllByIdIsIn(eventRequest.getRequestIds());

        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            return new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
        }

        return processRequests(event, eventRequest, requests);
    }

    private Event getEventOrThrow(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Событие с id " + eventId + " не найдено"));
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id " + userId + " не найден"));
    }

    private void initializeEventViewsAndRequests(Event event, HttpServletRequest request) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            addEventConfirmRequestAndSetViews(event, request);
        } else {
            event.setViews(0L);
            event.setConfirmedRequests(0L);
        }
    }

    private void validateParticipantLimit(Event event, Long eventId) {
        if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
            log.warn("Достигнут лимит по заявкам на данное событие с id: {}", eventId);
            throw new ForbiddenEventException("Достигнут лимит по заявкам на данное событие с id: " + eventId);
        }
    }

    private EventRequestStatusUpdateResult processRequests(Event event, EventRequestStatusUpdateRequest eventRequest, List<Request> requests) {
        if (eventRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
            return confirmRequests(event, requests);
        } else if (eventRequest.getStatus().equals(RequestStatus.REJECTED)) {
            List<ParticipationRequestDto> rejectedRequests = addRejectedRequests(requests);
            return new EventRequestStatusUpdateResult(new ArrayList<>(), rejectedRequests);
        }

        return new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
    }

    private EventRequestStatusUpdateResult confirmRequests(Event event, List<Request> requests) {
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        long count = processingEvents.confirmedRequestsForOneEvent(event, RequestStatus.CONFIRMED);
        event.setConfirmedRequests(count);

        for (Request request : requests) {
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new ConflictRequestException("Статус заявки " + request.getId() + " не позволяет ее одобрить, текущий статус " + request.getStatus());
            }

            if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                request.setStatus(RequestStatus.CONFIRMED);
                confirmedRequests.add(RequestMapper.requestToParticipationRequestDto(request));
                requestRepository.save(request);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1L);
            } else {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(RequestMapper.requestToParticipationRequestDto(request));
                requestRepository.save(request);
            }
        }

        eventRepository.save(event);
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

                /**
                 * Метод проверки времени и даты от текущего времени
                 *
                 * @param eventDate Время и дата из объекта события
                 */
    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate != null) {
            LocalDateTime timeNow = LocalDateTime.now().plusHours(2L);
            if (eventDate.isBefore(timeNow)) {
                throw new BadRequestException("Событие должно содержать дату, которая еще не наступила. " +
                        "Текущее значение: " + eventDate);
            }
        }
    }

    /**
     * Метод проверки пользователя на участие в своём событии
     *
     * @param user  Объект пользователя
     * @param event Объект события
     */
    private void checkOwnerEvent(Event event, User user) {
        if (!Objects.equals(event.getInitiator().getId(), user.getId())) {
            throw new ForbiddenEventException("Событие с id:" + event.getId() + " не принадлежит пользователю с id:" + user.getId());
        }
    }

    /**
     * Метод определения статуса события
     *
     * @param stateAction Текущий статус из объекта события
     * @return Новый статус после определения
     */
    private EventState determiningTheStatusForEvent(ActionState stateAction) {
        switch (stateAction) {
            case SEND_TO_REVIEW:
                return EventState.PENDING;
            case CANCEL_REVIEW:
            case REJECT_EVENT:
                return EventState.CANCELED;
            case PUBLISH_EVENT:
                return EventState.PUBLISHED;
            default:
                throw new BadRequestException("Статус не соответствует модификатору доступа");
        }
    }

    /**
     * Метод проверки доступности события
     *
     * @param event Объект события
     */
    private void eventAvailability(Event event) {
        if (event == null)
            throw new IllegalArgumentException("Событие не должно быть null");

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenEventException("Статус события не позволяет редактировать событие, статус: " + event.getState());
        }
    }

    /**
     * Метод добавления статуса запросов
     *
     * @param requests Список запросов
     * @return Список данных заявок на участие в событии
     */
    private List<ParticipationRequestDto> addRejectedRequests(List<Request> requests) {
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        for (Request requestList : requests) {
            if (!requestList.getStatus().equals(RequestStatus.PENDING)) {
                throw new ConflictRequestException("Статус заявки " + requestList.getId()
                        + " не получается ее одобрить, текущий статус " + requestList.getStatus());
            }
            requestList.setStatus(RequestStatus.REJECTED);
            requestRepository.save(requestList);
            rejectedRequests.add(RequestMapper.requestToParticipationRequestDto(requestList));
        }
        return rejectedRequests;
    }

    /**
     * Метод добавления подтверждённых событий
     *
     * @param event Объект события
     */
    private void addEventConfirmRequestAndSetViews(Event event, HttpServletRequest request) {
        if (event == null)
            throw new IllegalArgumentException("Событие не должно быть null");

        long count = processingEvents.confirmedRequestsForOneEvent(event, RequestStatus.CONFIRMED);
        event.setConfirmedRequests(count);
        long views = processingEvents.searchViews(event, request);
        event.setViews(views);
    }
}