package ru.practicum.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exceptions.ResourceNotFoundException;
import ru.practicum.models.Comment;
import ru.practicum.models.Event;
import ru.practicum.models.User;
import ru.practicum.models.enums.CommentState;

import java.util.List;

/**
 * Интерфейс CommentsRepository для обработки запросов к БД
 */
public interface CommentsRepository extends JpaRepository<Comment, Long> {
    /**
     * Находит комментарии по событию с учетом пагинации.
     *
     * @param event    событие, к которому относятся комментарии
     * @param pageable параметры пагинации
     * @return список комментариев
     */
    List<Comment> findByEvent(Event event, Pageable pageable);

    /**
     * Находит комментарии по событию, исключая комментарии с указанным состоянием.
     *
     * @param event    событие, к которому относятся комментарии
     * @param state    состояние комментария
     * @param pageable параметры пагинации
     * @return список комментариев
     */
    List<Comment> findByEventAndStateIsNot(Event event, CommentState state, Pageable pageable);

    /**
     * Находит комментарии по событию и автору с учетом пагинации.
     *
     * @param event    событие, к которому относятся комментарии
     * @param user     автор комментария
     * @param pageable параметры пагинации
     * @return список комментариев
     */
    List<Comment> findByEventAndAuthor(Event event, User user, Pageable pageable);

    /**
     * Получает комментарий по его идентификатору.
     *
     * @param id идентификатор комментария
     * @return комментарий с заданным идентификатором
     * @throws ResourceNotFoundException если комментарий не найден
     */
    default Comment get(long id) {
        return findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Комментарий c id:  " + id + " не существует"));
    }
}