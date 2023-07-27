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
        userRepository.existsById(userId);
        Pageable pageable = PageRequest.of(from, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);
        List<Event> eventsAddViews = processingEvents.addViewsInEventsList(events, request);
        List<Event> newEvents = processingEvents.confirmRequests(eventsAddViews);
        log.info("Получен приватный запрос на получение всех событий для пользователя с id: {}", userId);
        return newEvents.stream().map(EventMapper::eventToEventShortDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        NewEventDto tempNewEventDto = NewEventDto.builder()
                .annotation(newEventDto.getAnnotation())
                .category(newEventDto.getCategory())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid() != null && newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit() == null ? 0 : newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration() == null || newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .build();
        checkEventDate(DateFormatter.formatDate(tempNewEventDto.getEventDate()));
        User user = userRepository.get(userId);
        Category category = categoryRepository.get(tempNewEventDto.getCategory());
        Long views = 0L;
        Long confirmedRequests = 0L;
        Event event = EventMapper.newEventDtoToCreateEvent(tempNewEventDto, user, category, views, confirmedRequests);
        log.info("Получен приватный запрос на добавление события пользователем с id: {}", userId);
        return EventMapper.eventToEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto get(Long userId, Long eventId, HttpServletRequest request) {
        User user = userRepository.get(userId);
        Event event = eventRepository.get(eventId);
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
        try {
            Event event = eventRepository.get(eventId);
            User user = userRepository.get(userId);
            checkOwnerEvent(event, user);
            List<Request> requests = requestRepository.findAllByEvent(event);
            log.info("Получен приватный запрос на получение всех запросов для события с id: {} для пользователя с id: {}", eventId, userId);
            return requests.stream().map(RequestMapper::requestToParticipationRequestDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BadRequestException("Некорректный запрос получения списка запросов на участие в текущем событии");
        }
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateStatus(Long userId, Long eventId,
                                                       EventRequestStatusUpdateRequest eventRequest,
                                                       HttpServletRequest request) {
        Event event = eventRepository.get(eventId);
        User user = userRepository.get(userId);
        checkOwnerEvent(event, user);
        log.info("Получен приватный запрос на обновление статуса запроса для событие с id: {} для пользователя с id: {}", eventId, userId);
        if (event.getState().equals(EventState.PUBLISHED)) {
            addEventConfirmRequestAndSetViews(event, request);
        } else {
            event.setViews(0L);
            event.setConfirmedRequests(0L);
        }
        if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
            log.warn("Достигнут лимит по заявкам на данное событие с id: {}", eventId);
            throw new ForbiddenEventException("Достигнут лимит по заявкам на данное событие с id: " + eventId);
        }
        List<Request> requests = requestRepository.findAllByIdIsIn(eventRequest.getRequestIds());
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            return new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
        } else if (eventRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
            List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
            List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
            long count = processingEvents.confirmedRequestsForOneEvent(event, RequestStatus.CONFIRMED);
            event.setConfirmedRequests(count);
            for (Request requestList : requests) {
                if (!requestList.getStatus().equals(RequestStatus.PENDING)) {
                    throw new ConflictRequestException("Статус заявки " + requestList.getId()
                            + " не получается ее одобрить, текущий статус " + requestList.getStatus());
                }
                if (event.getConfirmedRequests() <= event.getParticipantLimit()) {
                    requestList.setStatus(RequestStatus.CONFIRMED);
                    confirmedRequests.add(RequestMapper.requestToParticipationRequestDto(requestList));
                    requestRepository.save(requestList);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1L);
                } else {
                    requestList.setStatus(RequestStatus.REJECTED);
                    rejectedRequests.add(RequestMapper.requestToParticipationRequestDto(requestList));
                    requestRepository.save(requestList);
                }
            }
            eventRepository.save(event);
            return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
        } else if (eventRequest.getStatus().equals(RequestStatus.REJECTED)) {
            EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
            List<ParticipationRequestDto> rejectedRequests = addRejectedRequests(requests);
            result.getRejectedRequests().addAll(rejectedRequests);
            return result;
        }
        return new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
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
        if (stateAction.equals(ActionState.SEND_TO_REVIEW)) {
            return EventState.PENDING;
        } else if (stateAction.equals(ActionState.CANCEL_REVIEW)) {
            return EventState.CANCELED;
        } else if (stateAction.equals(ActionState.PUBLISH_EVENT)) {
            return EventState.PUBLISHED;
        } else if (stateAction.equals(ActionState.REJECT_EVENT)) {
            return EventState.CANCELED;
        } else {
            throw new BadRequestException("Статус не соответствует модификатору доступа");
        }
    }

    /**
     * Метод проверки доступности события
     *
     * @param event Объект события
     */
    private void eventAvailability(Event event) {
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
        long count = processingEvents.confirmedRequestsForOneEvent(event, RequestStatus.CONFIRMED);
        event.setConfirmedRequests(count);
        long views = processingEvents.searchViews(event, request);
        event.setViews(views);
    }
}