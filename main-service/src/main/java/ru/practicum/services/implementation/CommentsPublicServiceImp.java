package ru.practicum.services.implementation;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mappers.CommentsMapper;
import ru.practicum.models.Comment;
import ru.practicum.models.Event;
import ru.practicum.models.dto.CommentDto;
import ru.practicum.models.enums.CommentState;
import ru.practicum.repositories.CommentsRepository;
import ru.practicum.repositories.EventRepository;
import ru.practicum.services.CommentsPublicService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс CommentsPublicServiceImp для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentsPublicServiceImp implements CommentsPublicService {

    private final EventRepository eventRepository;
    private final CommentsRepository commentsRepository;

    @Override
    public List<CommentDto> get(Long id, Integer from, Integer size) {
        Event event = eventRepository.get(id);
        Pageable pageable = PageRequest.of(from, size);
        List<Comment> comments = commentsRepository.findByEventAndStateIsNot(event, CommentState.CANCELED, pageable);
        log.info("Выполнен публичный поиск списка комментариев по событию с id: {}, from: {}, size {}", id, from, size);
        return comments.stream().map(CommentsMapper::commentToCommentDto).collect(Collectors.toList());
    }
}