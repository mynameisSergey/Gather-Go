package ru.practicum.models;

import lombok.*;
import ru.practicum.models.enums.CommentState;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Модель объекта Comment
 */
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text")
    private String text;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;
    @Enumerated(EnumType.STRING)
    private CommentState state;
}