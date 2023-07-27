package ru.practicum.services;


import ru.practicum.models.dto.EventFullDto;
import ru.practicum.models.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Интерфейс EventPublicService для обработки логики запросов из EventPublicController
 */
public interface EventPublicService {

    /**
     * Метод получения списка событий
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
    List<EventShortDto> get(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd,
                            boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request);

    /**
     * Метод получения подробной информации о событии по ID
     *
     * @param id ID события
     * @return Искомый объект события
     */
    EventFullDto get(Long id, HttpServletRequest request);
}