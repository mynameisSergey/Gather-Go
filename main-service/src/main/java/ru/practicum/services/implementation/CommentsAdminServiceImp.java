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
        log.info("Выполнен поиск комментария по id: {}", id);
        Comment comment = commentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Комментарий с id " + id + " не найден"));
        return CommentsMapper.commentToCommentDto(comment);
    }

    @Override
    public List<CommentDto> getListCommentDtoById(Long id, Integer from, Integer size) {
        if (from < 0 || size <= 0)
            throw new IllegalArgumentException("Параметры 'from' и 'size' должны быть положительными");

        Event event = eventRepository.get(id);
        Pageable pageable = PageRequest.of(from, size);
        List<Comment> comments = commentsRepository.findByEvent(event, pageable);
        log.info("Выполнен поиск списка комментариев по событию с id: {}, from: {}, size {}", id, from, size);
        return comments.stream().map(CommentsMapper::commentToCommentDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto createCommentDto(InputCommentDto inputCommentDto) {
        Event event = eventRepository.findById(inputCommentDto.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Событие с id " + inputCommentDto.getEventId() + " не найдено"));
        User user = userRepository.findById(inputCommentDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + inputCommentDto.getUserId() + " не найден"));

        Comment comment = CommentsMapper.createComment(inputCommentDto, user, event);
        log.info("Создан объект CommentDto для комментария с eventId: {}, userId: {}", inputCommentDto.getEventId(), inputCommentDto.getUserId());

        return CommentsMapper.commentToCommentDto(commentsRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto updateCommentDto(Long id, UpdateCommentAdmin updateComment) {
        Comment comment = commentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Комментарий с id " + id + " не найден"));

        Event event = eventRepository.findById(updateComment.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Событие с id " + updateComment.getEventId() + " не найдено"));

        if (!userRepository.existsById(updateComment.getUserId())) {
            throw new ResourceNotFoundException("Пользователь с id: " + updateComment.getUserId() + " не найден");
        }

        if (!comment.getEvent().getId().equals(event.getId())) {
            throw new ForbiddenEventException("Комментарий с id: " + comment.getId() + " не принадлежит событию с id: " + event.getId());
        }

        if (updateComment.getText() != null && !updateComment.getText().isBlank()) {
            comment.setText(updateComment.getText());
        }

        if (updateComment.getCommentStateDto() != null) {
            comment.setState(CommentsMapper.toCommentState(updateComment.getCommentStateDto()));
        }

        log.info("Изменен комментарий с commentId: {}, eventId: {}", id, event.getId());

        return CommentsMapper.commentToCommentDto(commentsRepository.save(comment));
    }


    @Override
    @Transactional
    public void deleteByCommentId(Long id) {
        Comment comment = commentsRepository.get(id);
        log.info("Удален комментарий с commentId: {}", id);
        commentsRepository.delete(comment);
    }
}