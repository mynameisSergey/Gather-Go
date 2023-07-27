package ru.practicum.services.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exp.stat.client.StatsClient;
import ru.practicum.exp.stat.dto.ViewStatsDto;
import ru.practicum.models.Event;
import ru.practicum.models.Request;
import ru.practicum.models.enums.RequestStatus;
import ru.practicum.repositories.RequestRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcessingEvents {
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;

    /**
     * Метод добавления просмотров в список событий
     *
     * @param events Список событий
     * @return Лист событий
     */
    public List<Event> addViewsInEventsList(List<Event> events, HttpServletRequest request) {
        List<String> uris = events.stream().map(e -> request.getRequestURI() + "/" + e.getId()).collect(Collectors.toList());
        LocalDateTime start = findStartDateTime(events);
        LocalDateTime end = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        List<ViewStatsDto> stats = statsClient.getStats(start, end, uris, true);
        fillEventViews(events, stats, request.getRequestURI());
        return events;
    }

    /**
     * Метод заполнения списка событий
     *
     * @param events  Список объектов Event
     * @param stats   Список объектов ViewStatsDto
     * @param baseUri Текстовое представление ссылки
     */
    private void fillEventViews(List<Event> events, List<ViewStatsDto> stats, String baseUri) {
        if (!stats.isEmpty()) {
            Map<String, Long> statsByUri = stats.stream().collect(Collectors.groupingBy(
                    ViewStatsDto::getUri, Collectors.summingLong(ViewStatsDto::getHits)));
            events.forEach(e -> {
                Long views = statsByUri.get(baseUri + "/" + e.getId());
                if (views != null) {
                    e.setViews(views);
                }
            });
        } else {
            events.forEach(e -> e.setViews(0L));
        }
    }

    /**
     * Метод подтверждения заявок на участие в данном событии
     *
     * @param events Список объектов Event
     * @return Список объектов Event
     */
    public List<Event> confirmRequests(List<Event> events) {
        Map<Event, Long> requestsPerEvent = requestRepository.findAllByEventInAndStatus(events, RequestStatus.CONFIRMED)
                .stream()
                .collect(Collectors.groupingBy(Request::getEvent, Collectors.counting()));
        List<Event> newEvents = new ArrayList<>();
        if (!requestsPerEvent.isEmpty()) {
            for (Event e : events) {
                long count = requestsPerEvent.get(e) == null ? 0 : requestsPerEvent.get(e);
                e.setConfirmedRequests(count);
                newEvents.add(e);
            }
        } else {
            newEvents.addAll(events);
            newEvents.forEach(e -> e.setConfirmedRequests(0L));
        }
        return newEvents;
    }

    /**
     * Метод получения количества запросов по событию и статусу
     *
     * @param event  Объект Event
     * @param status Объект RequestStatus
     * @return Значение количества запросов
     */
    public long confirmedRequestsForOneEvent(Event event, RequestStatus status) {
        return requestRepository.countRequestByEventAndStatus(event, status);
    }

    /**
     * Метод поиска просмотров
     *
     * @param event Объект Event
     * @return Значение количества просмотров
     */
    public long searchViews(Event event, HttpServletRequest request) {
        LocalDateTime date = LocalDateTime.of(LocalDate.of(1900, 1, 1), LocalTime.of(0, 0, 1));
        LocalDateTime start = event.getPublishedOn() == null ? date : event.getPublishedOn();
        List<ViewStatsDto> stats = statsClient.getStats(start, LocalDateTime.now(), List.of(request.getRequestURI()), true);
        return stats.stream().map(ViewStatsDto::getHits).reduce(0L, Long::sum);
    }

    /**
     * Метод проверки времени публикации событий
     *
     * @param events Список объектов событий
     * @return Время публикации
     */
    private LocalDateTime findStartDateTime(List<Event> events) {
        LocalDateTime start;
        Event event = events.stream().sorted(Comparator.comparing(Event::getCreatedOn)).collect(Collectors.toList()).get(0);
        if (event.getPublishedOn() == null) {
            start = LocalDateTime.of(LocalDate.of(1900, 1, 1), LocalTime.of(0, 0, 1));
        } else {
            start = event.getPublishedOn();
        }
        return start;
    }
}