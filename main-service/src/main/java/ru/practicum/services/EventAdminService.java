package ru.practicum.services;

import ru.practicum.models.dto.EventFullDto;
import ru.practicum.models.dto.UpdateEventAdminRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Интерфейс EventAdminService для обработки логики запросов из EventAdminController
 */
public interface EventAdminService {

    /**
     * Метод получения списка событий
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
    List<EventFullDto> get(List<Long> users, List<String> states, List<Long> categories,
                           String rangeStart, String rangeEnd, int from, int size, HttpServletRequest request);

    /**
     * Метод редактирования события и его статуса (отклонение/публикация)
     *
     * @param id                      ID события
     * @param updateEventAdminRequest Объект UpdateEventAdminRequest с изменениями
     * @return Отредактированный объект EventFullDto
     */
    EventFullDto update(Long id, UpdateEventAdminRequest updateEventAdminRequest, HttpServletRequest request);
}