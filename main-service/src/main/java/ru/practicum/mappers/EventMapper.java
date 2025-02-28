package ru.practicum.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.models.Category;
import ru.practicum.models.Event;
import ru.practicum.models.User;
import ru.practicum.models.dto.EventCommentDto;
import ru.practicum.models.dto.EventFullDto;
import ru.practicum.models.dto.EventShortDto;
import ru.practicum.models.dto.NewEventDto;
import ru.practicum.models.enums.EventState;
import ru.practicum.util.DateFormatter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Утилитарный класс EventMapper для преобразования Event / EventShortDto / EventFullDto / EventState
 */
@UtilityClass
public class EventMapper {

    /**
     * Преобразование Event в EventShortDto
     *
     * @param event Объект Event
     * @return Преобразованный объект EventShortDto
     */
    public EventShortDto eventToEventShortDto(Event event) {
        if (event == null)
            throw new IllegalArgumentException("Event cannot be null");

        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.userToUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    /**
     * Преобразование Event в EventFullDto
     *
     * @param event Объект Event
     * @return Преобразованный объект EventFullDto
     */
    public EventFullDto eventToEventFullDto(Event event) {
        if (event == null)
            throw new IllegalArgumentException("Event cannot be null");

        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.userToUserShortDto(event.getInitiator()))
                .location(LocationMapper.locationToLocationDto(event.getLocation()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState().name())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    /**
     * Преобразование нескольких параметров в Event
     *
     * @param newEventDto       Объект NewEventDto
     * @param user              Объект User
     * @param category          Объект Category
     * @param views             Количество просмотрев события
     * @param confirmedRequests Количество одобренных заявок на участие в данном событии
     * @return Преобразованный объект Event
     */
    public Event newEventDtoToCreateEvent(NewEventDto newEventDto, User user, Category category, Long views,
                                          Long confirmedRequests) {
        if (newEventDto == null || user == null || category == null)
            throw new IllegalArgumentException("NewEventDto, User, and Category cannot be null");

        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(confirmedRequests)
                .createdOn(dateTime)
                .description(newEventDto.getDescription())
                .eventDate(DateFormatter.formatDate(newEventDto.getEventDate()))
                .initiator(user)
                .location(LocationMapper.locationDtoToLocation(newEventDto.getLocation()))
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(newEventDto.getRequestModeration())
                .state(EventState.PENDING)
                .title(newEventDto.getTitle())
                .views(views)
                .build();
    }

    public EventCommentDto eventToEventCommentDto(Event event) {
        if (event == null)
            throw new IllegalArgumentException("Event cannot be null");

        return EventCommentDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.userToUserShortDto(event.getInitiator()))
                .title(event.getTitle())
                .build();
    }
}