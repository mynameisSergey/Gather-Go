package ru.practicum.models.dto;

import lombok.*;

import java.util.List;

/**
 * Модель объекта EventRequestStatusUpdateResult
 * (Результат подтверждения/отклонения заявок на участие в событии)
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}