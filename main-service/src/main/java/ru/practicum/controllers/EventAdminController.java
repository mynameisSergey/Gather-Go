package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.models.dto.EventFullDto;
import ru.practicum.models.dto.UpdateEventAdminRequest;
import ru.practicum.services.EventAdminService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Класс EventAdminController по энпоинту admin/events
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventAdminController {

    private final EventAdminService eventAdminService;

    /**
     * Метод (эндпоинт) получения списка событий
     *
     * @param users      Список id пользователей, чьи события нужно найти
     * @param states     Список состояний в которых находятся искомые события
     * @param categories Список id категорий в которых будет вестись поиск
     * @param rangeStart Дата и время не раньше которых должно произойти событие
     * @param rangeEnd   Дата и время не позже которых должно произойти событие
     * @param from       Количество событий, которые нужно пропустить для формирования текущего набора
     * @param size       Количество событий в наборе
     * @return Полученный список событий
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    List<EventFullDto> get(@RequestParam(required = false) List<Long> users,
                           @RequestParam(required = false) List<String> states,
                           @RequestParam(required = false) List<Long> categories,
                           @RequestParam(required = false) String rangeStart,
                           @RequestParam(required = false) String rangeEnd,
                           @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                           @Positive @RequestParam(defaultValue = "10") Integer size,
                           HttpServletRequest request) {

        return eventAdminService.get(users, states, categories, rangeStart, rangeEnd, from, size, request);
    }

    /**
     * Метод (эндпоинт) редактирования события и его статуса (отклонение/публикация)
     *
     * @param eventId                 ID события
     * @param updateEventAdminRequest Объект UpdateEventAdminRequest с изменениями
     * @return Отредактированный объект EventFullDto
     */
    @PatchMapping("/{eventId}")
    EventFullDto update(@PathVariable Long eventId,
                        @Validated @RequestBody UpdateEventAdminRequest updateEventAdminRequest,
                        HttpServletRequest request) {
        return eventAdminService.update(eventId, updateEventAdminRequest, request);
    }

}