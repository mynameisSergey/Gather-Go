package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.models.dto.ParticipationRequestDto;
import ru.practicum.services.RequestPrivateService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Класс RequestPrivateController по энпоинту users/{userId}/requests
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")

public class RequestPrivateController {

    private final RequestPrivateService requestPrivateService;

    /**
     * Метод (эндпоинт) получения информации о заявках текущего пользователя на участие в чужих событиях
     *
     * @param userId ID пользователя
     * @return Список заявок на участие в событиях
     */
    @GetMapping
    List<ParticipationRequestDto> get(@PathVariable Long userId) {
        return requestPrivateService.get(userId);
    }

    /**
     * Метод (эндпоинт) добавление запроса от текущего пользователя на участие в событии
     *
     * @param userId  ID пользователя
     * @param eventId ID события
     * @return Созданный запрос на участие в событии
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ParticipationRequestDto create(@PathVariable Long userId,
                                   @NotNull @RequestParam Long eventId,
                                   HttpServletRequest request) {
        return requestPrivateService.create(userId, eventId, request);
    }

    /**
     * Метод (эндпоинт) отмены своего запроса на участие в событии
     *
     * @param userId    ID пользователя
     * @param requestId ID запроса
     * @return Изменённая заявка на участие в событии
     */
    @PatchMapping("/{requestId}/cancel")
    ParticipationRequestDto update(@PathVariable Long userId,
                                   @PathVariable Long requestId,
                                   HttpServletRequest request) {
        return requestPrivateService.update(userId, requestId, request);
    }

}