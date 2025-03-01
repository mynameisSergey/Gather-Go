package ru.practicum.services;


import ru.practicum.exceptions.model.NotFoundException;
import ru.practicum.models.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Интерфейс RequestPrivateService для обработки логики запросов из RequestPrivateController
 */
public interface RequestPrivateService {

    /**
     * Метод получения информации о заявках текущего пользователя на участие в чужих событиях
     *
     * @param id ID пользователя
     * @return Список заявок на участие в событиях
     */
    List<ParticipationRequestDto> get(Long id) throws NotFoundException;

    /**
     * Метод добавление запроса от текущего пользователя на участие в событии
     *
     * @param userId  ID пользователя
     * @param eventId ID события
     * @return Созданный запрос на участие в событии
     */
    ParticipationRequestDto create(Long userId, Long eventId, HttpServletRequest request) throws NotFoundException;

    /**
     * Метод отмены своего запроса на участие в событии
     *
     * @param userId    ID пользователя
     * @param requestId ID запроса
     * @return Изменённая заявка на участие в событии
     */
    ParticipationRequestDto update(Long userId, Long requestId, HttpServletRequest request);
}