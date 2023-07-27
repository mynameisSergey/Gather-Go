package ru.practicum.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.models.Comment;
import ru.practicum.models.Event;
import ru.practicum.models.User;
import ru.practicum.models.dto.CommentDto;
import ru.practicum.models.dto.InputCommentDto;
import ru.practicum.models.enums.CommentState;
import ru.practicum.models.enums.CommentStateDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Утилитарный класс CommentsMapper для преобразования Comment / InputCommentDto / CommentDto
 */
@UtilityClass
public class CommentsMapper {

    /**
     * Преобразование InputCommentDto в Comment
     *
     * @param inputCommentDto Объект InputCommentDto
     * @param user            Объект user
     * @param event           Объект event
     * @return Преобразованный объект Comment
     */
    public Comment createComment(InputCommentDto inputCommentDto, User user, Event event) {
        return Comment.builder()
                .text(inputCommentDto.getText())
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .author(user)
                .event(event)
                .state(CommentState.PUBLISHED)
                .build();
    }

    /**
     * Преобразование Comment в CommentDto
     *
     * @param comment Объект Comment
     * @return Преобразованный CommentDto
     */
    public CommentDto commentToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdOn(comment.getCreatedOn())
                .author(UserMapper.userToUserShortDto(comment.getAuthor()))
                .event(EventMapper.eventToEventCommentDto(comment.getEvent()))
                .state(comment.getState().name())
                .build();

    }

    /**
     * Преобразование составных элементов владельца комментария (изменение комментария) в Comment
     *
     * @param commentId ID комментария
     * @param text      Текст комментария
     * @param user      Объект user
     * @param event     Объект event
     * @return Преобразованный Comment
     */
    public Comment updateComment(Long commentId, String text, User user, Event event) {
        return Comment.builder()
                .id(commentId)
                .text(text)
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .author(user)
                .event(event)
                .state(CommentState.UPDATE)
                .build();
    }

    /**
     * Метод определения статуса комментария
     *
     * @param commentStateDto Объект CommentStateDto
     * @return Статус комментария
     */
    public CommentState toCommentState(CommentStateDto commentStateDto) {
        if (commentStateDto.name().equals(CommentState.UPDATE.name())) {
            return CommentState.UPDATE;
        }
        if (commentStateDto.name().equals(CommentState.PUBLISHED.name())) {
            return CommentState.PUBLISHED;
        }
        if (commentStateDto.name().equals(CommentState.CANCELED.name())) {
            return CommentState.CANCELED;
        } else {
            throw new BadRequestException("Статус не соответствует возможному: " + commentStateDto.name());
        }
    }
}