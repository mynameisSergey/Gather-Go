package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.models.dto.CommentDto;
import ru.practicum.models.dto.InputCommentDto;
import ru.practicum.models.dto.UpdateCommentAdmin;
import ru.practicum.services.CommentsAdminService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Класс CommentsAdminController по энпоинту admin/comments
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
public class CommentsAdminController {

    private final CommentsAdminService commentsAdminService;

    /**
     * Метод (эндпоинт) получения комментария по ID
     *
     * @param commentId ID комментария
     * @return Объект CommentDto
     */
    @GetMapping("/{commentId}")
    public CommentDto get(@PathVariable Long commentId) {
        return commentsAdminService.get(commentId);
    }

    /**
     * Метод (эндпоинт) получения списка комментариев по ID события
     *
     * @param eventId ID события
     * @param from    Количество комментариев, которые нужно пропустить для формирования текущего набора
     * @param size    Количество комментариев в наборе
     * @return Список комментариев по событию
     */
    @GetMapping("/event/{eventId}")
    public List<CommentDto> get(@PathVariable Long eventId,
                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        return commentsAdminService.get(eventId, from, size);
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
        return commentsAdminService.create(inputCommentDto);
    }

    /**
     * Метод (эндпоинт) изменения комментария по ID
     *
     * @param commentId     ID комментария
     * @param updateComment Новый изменённый комментарий в виде объекта UpdateCommentAdminDto
     * @return Изменённый комментарий в виде объекта CommentDto
     */
    @PatchMapping("/{commentId}")
    public CommentDto update(@PathVariable Long commentId,
                             @Validated @RequestBody UpdateCommentAdmin updateComment) {
        return commentsAdminService.update(commentId, updateComment);
    }

    /**
     * Метод (эндпоинт) удаления комментария по ID
     *
     * @param commentId ID комментария
     */
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId) {
        commentsAdminService.delete(commentId);
    }
}