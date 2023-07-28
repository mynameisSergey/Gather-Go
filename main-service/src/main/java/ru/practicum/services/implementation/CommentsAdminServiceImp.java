package ru.practicum.services.implementation;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.ForbiddenEventException;
import ru.practicum.exceptions.ResourceNotFoundException;
import ru.practicum.mappers.CommentsMapper;
import ru.practicum.models.Comment;
import ru.practicum.models.Event;
import ru.practicum.models.User;
import ru.practicum.models.dto.CommentDto;
import ru.practicum.models.dto.InputCommentDto;
import ru.practicum.models.dto.UpdateCommentAdmin;
import ru.practicum.repositories.CommentsRepository;
import ru.practicum.repositories.EventRepository;
import ru.practicum.repositories.UserRepository;
import ru.practicum.services.CommentsAdminService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс CommentsAdminServiceImp для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentsAdminServiceImp implements CommentsAdminService {

    private final EventRepository eventRepository;
    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;

    @Override
    public CommentDto getCommentDtoById(Long id) {
        log.info("Получен запрос на поиск комментария по id: {}", id);
        return CommentsMapper.commentToCommentDto(commentsRepository.get(id));
    }

    @Override
    public List<CommentDto> getListCommentDtoById(Long id, Integer from, Integer size) {
        Event event = eventRepository.get(id);
        Pageable pageable = PageRequest.of(from, size);
        List<Comment> comments = commentsRepository.findByEvent(event, pageable);
        log.info("Получен запрос на список всех комментариев по событию с id: {}, from: {}, size {}", id, from, size);
        return comments.stream().map(CommentsMapper::commentToCommentDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto create(InputCommentDto inputCommentDto) {
        Event event = eventRepository.get(inputCommentDto.getEventId());
        User user = userRepository.get(inputCommentDto.getUserId());
        Comment comment = CommentsMapper.createComment(inputCommentDto, user, event);
        log.info("Получен запрос на добавления комментария к eventId: {}, userId: {}",
                inputCommentDto.getEventId(), inputCommentDto.getUserId());
        return CommentsMapper.commentToCommentDto(commentsRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto update(Long id, UpdateCommentAdmin updateComment) {
        Event event = eventRepository.get(updateComment.getEventId());
        if (!userRepository.existsById(updateComment.getUserId())) {
            throw new ResourceNotFoundException("Пользователь c id: " + updateComment.getUserId() + " не найден");
        }
        Comment comment = commentsRepository.get(id);
        if (!comment.getEvent().getId().equals(event.getId())) {
            throw new ForbiddenEventException("Комментарий с id: " + comment.getId()
                    + " не принадлежит событию с id: " + event.getId());
        }
        if (updateComment.getText() != null && !updateComment.getText().isBlank()) {
            comment.setText(updateComment.getText());
        }
        if (updateComment.getCommentStateDto() != null) {
            comment.setState(CommentsMapper.toCommentState(updateComment.getCommentStateDto()));
        }
        log.info("Получен запрос на изменения комментария к commentId: {}, eventId: {}", id, event.getId());
        return CommentsMapper.commentToCommentDto(commentsRepository.save(comment));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Comment comment = commentsRepository.get(id);
        log.info("Получен запрос на удаления комментария к commentId: {}", id);
        commentsRepository.delete(comment);
    }
}