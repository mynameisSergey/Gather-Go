package ru.practicum.models.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.models.enums.RequestStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Модель объекта EventRequestStatusUpdateRequest
 * (Изменение статуса запроса на участие в событии текущего пользователя)
 */
@Value
@Builder
public class EventRequestStatusUpdateRequest {
    @NotEmpty(message = "Список идентификаторов запросов не должен быть пустым")
    List<Long> requestIds;
    @NotNull(message = "Статус не должен быть null")
    RequestStatus status;
}