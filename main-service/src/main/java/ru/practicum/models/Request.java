package ru.practicum.models;

import lombok.*;
import ru.practicum.models.enums.RequestStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Модель объекта Request
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created", nullable = false)
    @NotNull(message = "Creation time must not be null")
    private LocalDateTime created;
    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    @NotNull(message = "Event must not be null")
    private Event event;
    @ManyToOne(optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    @NotNull(message = "Requester must not be null")
    private User requester;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Request status must not be null")
    private RequestStatus status;
}