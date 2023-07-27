package ru.practicum.services;


import ru.practicum.models.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Интерфейс EventPrivateService для обработки логики запросов из EventPrivateController
 */
public interface EventPrivateService {

    /**
     * Метод получения списка событий добавленных текущим пользователем
     *
     * @param userId ID текущего пользователя
     * @param from   Количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size   Количество элементов в наборе
     * @return Список событий
     */
    List<EventShortDto> get(Long userId, int from, int size, HttpServletRequest request);

    /**
     * Метод добавления нового события
     *
     * @param userId      ID текущего пользователя
     * @param newEventDto Объект NewEventDto
     * @return Объект добавленного события EventFullDto
     */
    EventFullDto create(Long userId, NewEventDto newEventDto);

    /**
     * Метод получения полной информации о событии добавленном текущим пользователем
     *
     * @param userId  ID текущего пользователя
     * @param eventId ID события
     * @return Искомый объект EventFullDto
     */
    EventFullDto get(Long userId, Long eventId, HttpServletRequest request);

    /**
     * Метод изменения события добавленного текущим пользователем
     *
     * @param userId                 ID текущего пользователя
     * @param eventId                ID события
     * @param updateEventUserRequest Объект события UpdateEventUserRequest
     * @return Изменённый объект события EventFullDto
     */
    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest, HttpServletRequest request);

    /**
     * Метод получения списка запросов на участие в событии текущего пользователя
     *
     * @param userId  ID текущего пользователя
     * @param eventId ID события
     * @return Список заявок на участие
     */
    List<ParticipationRequestDto> getRequests(Long userId, Long eventId, HttpServletRequest request);

    /**
     * Метод изменения статуса (подтверждена/отменена) заявок на участие в событии текущего пользователя
     *
     * @param userId       ID текущего пользователя
     * @param eventId      ID события
     * @param eventRequest Изменение статуса запроса на участие в событии текущего пользователя
     * @return Результат подтверждения/отклонения заявок на участие в событии
     */
    EventRequestStatusUpdateResult updateStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequest, HttpServletRequest request);
}