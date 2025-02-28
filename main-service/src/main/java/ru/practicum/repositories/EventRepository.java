package ru.practicum.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.exceptions.ResourceNotFoundException;
import ru.practicum.models.Category;
import ru.practicum.models.Event;
import ru.practicum.models.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Интерфейс EventRepository для обработки запросов к БД
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    /**
     * Находит события по категории.
     *
     * @param category категория события
     * @return список событий
     */
    List<Event> findEventByCategoryIs(Category category);

    /**
     * Находит события по их идентификаторам.
     *
     * @param id идентификаторы событий
     * @return множество событий
     */
    Set<Event> findAllByIdIsIn(List<Long> id);

    /**
     * Находит все события по идентификатору инициатора.
     *
     * @param userId   идентификатор инициатора
     * @param pageable параметры пагинации
     * @return список событий
     */
    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    /**
     * Находит все события для администраторов с учетом различных фильтров.
     *
     * @param users      список идентификаторов пользователей
     * @param states     список состояний событий
     * @param categories список категорий событий
     * @param rangeStart начало диапазона дат
     * @param rangeEnd   конец диапазона дат
     * @param from       смещение для пагинации
     * @param size       размер страницы
     * @return список событий
     */
    @Query(value = "SELECT * FROM events WHERE (initiator_id IN :users OR :users IS NULL) AND state IN :states " +
            "AND (category_id IN :categories  OR :categories IS NULL) AND (event_date >= to_timestamp(:rangeStart, 'yyyy-mm-dd hh24:mi:ss')  " +
            "OR to_timestamp(:rangeStart, 'yyyy-mm-dd hh24:mi:ss') IS NULL) AND (event_date <= to_timestamp(:rangeEnd, 'yyyy-mm-dd hh24:mi:ss')   " +
            "OR to_timestamp(:rangeEnd, 'yyyy-mm-dd hh24:mi:ss') IS NULL) OFFSET :from LIMIT :size", nativeQuery = true)
    List<Event> findAllByAdmin(@Param("users") List<Long> users,
                               @Param("states") List<String> states,
                               @Param("categories") List<Long> categories,
                               @Param("rangeStart") LocalDateTime rangeStart,
                               @Param("rangeEnd") LocalDateTime rangeEnd,
                               @Param("from") Integer from,
                               @Param("size") Integer size);

    /**
     * Находит все события для администраторов с учетом фильтров и пагинации.
     *
     * @param users      список идентификаторов пользователей
     * @param states     список состояний событий
     * @param categories список категорий событий
     * @param rangeStart начало диапазона дат
     * @param rangeEnd   конец диапазона дат
     * @param pageable   параметры пагинации
     * @return список событий
     */
    @Query("select e from Event e " +
            "where (:users is null or e.initiator.id in :users) " +
            "and (:states is null or e.state in :states) " +
            "and (:categories is null or e.category.id in :categories) " +
            "and (cast(:rangeStart as java.time.LocalDateTime) is null or e.eventDate >= :rangeStart) " +
            "and (cast(:rangeEnd as java.time.LocalDateTime) is null or e.eventDate <= :rangeEnd)")
    List<Event> findAllByAdminAndState(@Param("users") List<Long> users,
                                       @Param("states") List<EventState> states,
                                       @Param("categories") List<Long> categories,
                                       @Param("rangeStart") LocalDateTime rangeStart,
                                       @Param("rangeEnd") LocalDateTime rangeEnd,
                                       Pageable pageable);

    /**
     * Находит события для публичного доступа с учетом фильтров.
     *
     * @param text       текст для поиска в аннотации или описании
     * @param categories категории событий
     * @param paid       статус оплаты
     * @param rangeStart начало диапазона дат
     * @param rangeEnd   конец диапазона дат
     * @param sort       критерий сортировки
     * @param from       смещение для пагинации
     * @param size       размер страницы
     * @return список событий
     */
    @Query(value = "SELECT * " +
            "FROM events  " +
            "WHERE (lower(annotation) LIKE '%'||lower(:text)||'%' OR lower(description) LIKE '%'||lower(:text)||'%') " +
            "AND (category_id IN :categories  OR :categories IS NULL) " +
            "AND (:paid IS NULL OR paid = :paid) " +
            "AND (event_date BETWEEN " +
            "to_timestamp(:rangeStart, 'yyyy-mm-dd hh24:mi:ss') AND to_timestamp(:rangeEnd, 'yyyy-mm-dd hh24:mi:ss') " +
            "OR event_date > CURRENT_TIMESTAMP) " +
            "ORDER BY lower(:sort) " +
            "OFFSET :from " +
            "LIMIT :size", nativeQuery = true)
    List<Event> findAllByPublic(@Param("text") String text,
                                @Param("categories") List<Long> categories,
                                @Param("paid") Boolean paid,
                                @Param("rangeStart") String rangeStart,
                                @Param("rangeEnd") String rangeEnd,
                                @Param("sort") String sort,
                                @Param("from") Integer from,
                                @Param("size") Integer size);

    /**
     * Находит событие по его идентификатору и состоянию.
     *
     * @param id    идентификатор события
     * @param state состояние события
     * @return событие, если найдено
     */
    Optional<Event> findEventByIdAndStateIs(Long id, EventState state);

    default Event get(long id) {
        return findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Событие c id:  " + id + " не существует"));
    }
}