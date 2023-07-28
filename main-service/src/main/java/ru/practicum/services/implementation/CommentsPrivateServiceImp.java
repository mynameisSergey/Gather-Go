package ru.practicum.services.implementation;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ForbiddenEventException;
import ru.practicum.mappers.CommentsMapper;
import ru.practicum.models.Comment;
import ru.practicum.models.Event;
import ru.practicum.models.User;
import ru.practicum.models.dto.CommentDto;
import ru.practicum.models.dto.InputCommentDto;
import ru.practicum.models.enums.CommentState;
import ru.practicum.repositories.CommentsRepository;
import ru.practicum.repositories.EventRepository;
import ru.practicum.repositories.UserRepository;
import ru.practicum.services.CommentsPrivateService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс CommentsPrivateServiceImp для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentsPrivateServiceImp implements CommentsPrivateService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentsRepository commentsRepository;

    @Override
    public CommentDto getCommentDtoByCommentId(Long commentId, Long userId) {
        Comment comment = commentsRepository.get(commentId);
        User user = userRepository.get(userId);
        checkCommentOnOwner(comment, user);
        log.info("Выполнен приватный поиск комментария с commentId: {}, userId: {}", commentId, userId);
        return CommentsMapper.commentToCommentDto(comment);
    }

    @Override
    public List<CommentDto> getListCommentDtoByEventId(Long eventId, Long userId, Integer from, Integer size) {
        Event event = eventRepository.get(eventId);
        User user = userRepository.get(userId);
        Pageable pageable = PageRequest.of(from, size);
        List<Comment> comments = commentsRepository.findByEventAndAuthor(event, user, pageable);
        log.info("Выполнен приватный поиск списка комментариев по eventId: {}, userId: {}, from: {}, size {}",
                eventId, userId, from, size);
        return comments.stream().map(CommentsMapper::commentToCommentDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto createCommentDto(InputCommentDto inputCommentDto) {
        Event event = eventRepository.get(inputCommentDto.getEventId());
        User user = userRepository.get(inputCommentDto.getUserId());
        Comment comment = CommentsMapper.createComment(inputCommentDto, user, event);
        log.info("Приватно создан объект CommentDto для комментария с eventId: {}, userId: {}",
                inputCommentDto.getEventId(), inputCommentDto.getUserId());
        return CommentsMapper.commentToCommentDto(commentsRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto updateCommentDtoByCommentId(Long commentId, InputCommentDto inputCommentDto) {
        Comment comment = commentsRepository.get(commentId);
        if (comment.getState() == CommentState.CANCELED) {
            throw new BadRequestException("Комментарий с id:" + comment.getId() + " ранее был отменен");
        }
        Event event = eventRepository.get(inputCommentDto.getEventId());
        User user = userRepository.get(inputCommentDto.getUserId());
        if (!comment.getEvent().getId().equals(event.getId())) {
            throw new ForbiddenEventException("Комментарий с id: " + comment.getId()
                    + " не принадлежит событию с id: " + event.getId());
        }
        checkCommentOnOwner(comment, user);
        Comment newComment = CommentsMapper.updateComment(comment.getId(), inputCommentDto.getText(), user, event);
        log.info("Приватно изменен комментарий с commentId: {}, eventId: {}", commentId, event.getId());
        return CommentsMapper.commentToCommentDto(commentsRepository.save(newComment));
    }

    @Override
    @Transactional
    public void deleteCommentDtoByCommentId(Long commentId, Long userId) {
        User user = userRepository.get(userId);
        Comment comment = commentsRepository.get(commentId);
        checkCommentOnOwner(comment, user);
        log.info("Приватно удален комментарий с commentId: {}, userId: {}", commentId, userId);
        commentsRepository.delete(comment);
    }

    /**
     * Метод проверки комментария на принадлежность его пользователю
     *
     * @param comment Объект Comment
     * @param user    Объект User
     */
    public void checkCommentOnOwner(Comment comment, User user) {
        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new ForbiddenEventException("Комментарий с id:" + comment.getId()
                    + " не принадлежит пользователю с id:" + user.getId());
        }
    }
}