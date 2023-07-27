package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.models.dto.CommentDto;
import ru.practicum.services.CommentsPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Класс CommentsPublicController по энпоинту comments/event/{eventId}
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments/event/{eventId}")
public class CommentsPublicController {

    private final CommentsPublicService commentsPublicService;

    /**
     * Метод (эндпоинт) получения списка комментариев по ID события
     *
     * @param eventId ID события
     * @param from    Количество комментариев, которые нужно пропустить для формирования текущего набора
     * @param size    Количество комментариев в наборе
     * @return Список комментариев по событию
     */
    @GetMapping()
    public List<CommentDto> get(@PathVariable Long eventId,
                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        return commentsPublicService.get(eventId, from, size);
    }
}