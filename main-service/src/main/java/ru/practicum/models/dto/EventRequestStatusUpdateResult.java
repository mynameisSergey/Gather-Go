package ru.practicum.models.dto;

import lombok.*;

import java.util.List;

/**
 * Модель объекта EventRequestStatusUpdateResult
 * (Результат подтверждения/отклонения заявок на участие в событии)
 */
@Builder
@Data
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}