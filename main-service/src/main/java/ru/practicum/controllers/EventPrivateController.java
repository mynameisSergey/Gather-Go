package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.models.dto.*;
import ru.practicum.services.EventPrivateService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Класс EventPrivateController по энпоинту users/{userId}/events
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {

    private final EventPrivateService eventPrivateService;

    /**
     * Метод (эндпоинт) получения списка событий добавленных текущим пользователем
     *
     * @param userId ID текущего пользователя
     * @param from   Количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size   Количество элементов в наборе
     * @return Список событий
     */
    @GetMapping()
    List<EventShortDto> get(@PathVariable Long userId,
                            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                            @Positive @RequestParam(defaultValue = "10") Integer size,
                            HttpServletRequest request) {
        return eventPrivateService.get(userId, from, size, request);
    }

    /**
     * Метод (эндпоинт) добавления нового события
     *
     * @param userId      ID текущего пользователя
     * @param newEventDto Объект NewEventDto
     * @return Объект добавленного события EventFullDto
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    EventFullDto create(@PathVariable Long userId,
                        @Validated @RequestBody NewEventDto newEventDto) {
        return eventPrivateService.create(userId, newEventDto);
    }

    /**
     * Метод (эндпоинт) получения полной информации о событии добавленном текущим пользователем
     *
     * @param userId  ID текущего пользователя
     * @param eventId ID события
     * @return Искомый объект EventFullDto
     */
    @GetMapping("/{eventId}")
    EventFullDto get(@PathVariable Long userId,
                     @PathVariable Long eventId,
                     HttpServletRequest request) {
        return eventPrivateService.get(userId, eventId, request);
    }

    /**
     * Метод (эндпоинт) изменения события добавленного текущим пользователем
     *
     * @param userId                 ID текущего пользователя
     * @param eventId                ID события
     * @param updateEventUserRequest Объект события UpdateEventUserRequest
     * @return Изменённый объект события EventFullDto
     */
    @PatchMapping("/{eventId}")
    EventFullDto update(@PathVariable Long userId,
                        @PathVariable Long eventId,
                        @Validated @RequestBody UpdateEventUserRequest updateEventUserRequest,
                        HttpServletRequest request) {
        return eventPrivateService.update(userId, eventId, updateEventUserRequest, request);
    }

    /**
     * Метод (эндпоинт) получения списка запросов на участие в событии текущего пользователя
     *
     * @param userId  ID текущего пользователя
     * @param eventId ID события
     * @return Список заявок на участие
     */
    @GetMapping("/{eventId}/requests")
    List<ParticipationRequestDto> getRequests(@PathVariable Long userId,
                                              @PathVariable Long eventId,
                                              HttpServletRequest request) {
        return eventPrivateService.getRequests(userId, eventId, request);
    }

    /**
     * Метод (эндпоинт) изменения статуса (подтверждена/отменена) заявок на участие в событии текущего пользователя
     *
     * @param userId       ID текущего пользователя
     * @param eventId      ID события
     * @param eventRequest Изменение статуса запроса на участие в событии текущего пользователя
     * @return Результат подтверждения/отклонения заявок на участие в событии
     */
    @PatchMapping("/{eventId}/requests")
    EventRequestStatusUpdateResult updateStatus(@PathVariable Long userId,
                                                @PathVariable Long eventId,
                                                @RequestBody EventRequestStatusUpdateRequest eventRequest,
                                                HttpServletRequest request) {
        return eventPrivateService.updateStatus(userId, eventId, eventRequest, request);
    }
}