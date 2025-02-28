package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.models.dto.EventFullDto;
import ru.practicum.models.dto.EventShortDto;
import ru.practicum.services.EventPublicService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Класс EventPublicController по энпоинту events
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventPublicController {

    private final EventPublicService eventPublicService;

    /**
     * Метод (эндпоинт) получения списка событий
     *
     * @param text          Текст для поиска в содержимом аннотации и подробном описании события
     * @param categories    Список идентификаторов категорий в которых будет вестись поиск
     * @param paid          Поиск только платных/бесплатных событий
     * @param rangeStart    Дата и время не раньше которых должно произойти событие
     * @param rangeEnd      Дата и время не позже которых должно произойти событие
     * @param onlyAvailable Только события у которых не исчерпан лимит запросов на участие
     * @param sort          Вариант сортировки: по дате события или по количеству просмотров
     * @param from          Количество событий, которые нужно пропустить для формирования текущего набора
     * @param size          Количество событий в наборе
     * @return Полученный список событий
     */
    @GetMapping()
    List<EventShortDto> get(@RequestParam(required = false) String text,
                            @RequestParam(required = false) List<Long> categories,
                            @RequestParam(required = false) Boolean paid,
                            @RequestParam(required = false) String rangeStart,
                            @RequestParam(required = false) String rangeEnd,
                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                            @RequestParam(defaultValue = "EVENT_DATE") String sort,
                            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                            @Positive @RequestParam(defaultValue = "10") Integer size,
                            HttpServletRequest request) {
        return eventPublicService.get(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, request);
    }

    /**
     * Метод (эндпоинт) получения подробной информации о событии по ID
     *
     * @param id ID события
     * @return Искомый объект события
     */
    @GetMapping("/{id}")
    EventFullDto get(@PathVariable Long id, HttpServletRequest request) {
        return eventPublicService.getEventFullDto(id, request);
    }

}