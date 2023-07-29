package ru.practicum.models.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.models.enums.RequestStatus;

import java.util.List;

/**
 * Модель объекта EventRequestStatusUpdateRequest
 * (Изменение статуса запроса на участие в событии текущего пользователя)
 */
@Value
@Builder
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private RequestStatus status;
}