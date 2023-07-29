package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.models.dto.CommentDto;
import ru.practicum.models.dto.InputCommentDto;
import ru.practicum.services.CommentsPrivateService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Класс CommentsPrivateController по энпоинту private/comments
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/private/comments")
public class CommentsPrivateController {

    private final CommentsPrivateService commentsPrivateService;

    /**
     * Метод (эндпоинт) получения комментария по ID комментария и ID пользователя
     *
     * @param commentId ID комментария
     * @param userId    ID пользователя
     * @return Объект CommentDto
     */
    @GetMapping("/{commentId}/user/{userId}")
    public CommentDto get(@PathVariable Long commentId,
                          @PathVariable Long userId) {
        return commentsPrivateService.getCommentDtoByCommentId(commentId, userId);
    }

    /**
     * Метод (эндпоинт) получения списка комментариев по ID события и ID пользователя
     *
     * @param eventId ID события
     * @param userId  ID пользователя
     * @param from    Количество комментариев, которые нужно пропустить для формирования текущего набора
     * @param size    Количество комментариев в наборе
     * @return Список комментариев по событию
     */
    @GetMapping("/event/{eventId}/user/{userId}")
    public List<CommentDto> get(@PathVariable Long eventId,
                                @PathVariable Long userId,
                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        return commentsPrivateService.getListCommentDtoByEventId(eventId, userId, from, size);
    }

    /**
     * Метод (эндпоинт) добавления комментария
     *
     * @param inputCommentDto Новый комментарий в виде объекта InputCommentDto
     * @return Добавленный комментарий в виде объекта CommentDto
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@Validated @RequestBody InputCommentDto inputCommentDto) {
        return commentsPrivateService.createCommentDto(inputCommentDto);
    }

    /**
     * Метод (эндпоинт) изменения комментария по ID
     *
     * @param commentId       ID комментария
     * @param inputCommentDto Новый изменённый комментарий в виде объекта InputCommentDto
     * @return Изменённый комментарий в виде объекта CommentDto
     */
    @PatchMapping("/{commentId}")
    public CommentDto update(@PathVariable Long commentId,
                             @Validated @RequestBody InputCommentDto inputCommentDto) {
        return commentsPrivateService.updateCommentDtoByCommentId(commentId, inputCommentDto);
    }

    /**
     * Метод (эндпоинт) удаления по ID комментария и ID пользователя
     *
     * @param commentId ID комментария
     * @param userId    ID пользователя
     */
    @DeleteMapping("/{commentId}/user/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId,
                       @PathVariable Long userId) {
        commentsPrivateService.deleteCommentDtoByCommentId(commentId, userId);
    }
}