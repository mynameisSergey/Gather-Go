package ru.practicum.exp.stat.serv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exp.stat.dto.ViewStatsDto;
import ru.practicum.exp.stat.serv.models.Hit;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Интерфейс StatsRepository для обработки запросов к БД
 */
public interface StatsRepository extends JpaRepository<Hit, Long> {

    /**
     * Метод получения списка посещений по времени начала и конца
     *
     * @param start Дата и время начала диапазона за который нужно выгрузить статистику
     * @param end   Дата и время конца диапазона за который нужно выгрузить статистику
     * @return Сформированный список статистики по посещениям
     */
    @Query("SELECT NEW ru.practicum.exp.stat.dto.ViewStatsDto(h.app, h.uri, COUNT (h.ip)) FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.ip) DESC")
    List<ViewStatsDto> findByDate(LocalDateTime start, LocalDateTime end);

    /**
     * Метод получения списка посещений по времени начала и конца только с уникальным ip
     *
     * @param start Дата и время начала диапазона за который нужно выгрузить статистику
     * @param end   Дата и время конца диапазона за который нужно выгрузить статистику
     * @return Сформированный список статистики по посещениям
     */
    @Query("SELECT NEW ru.practicum.exp.stat.dto.ViewStatsDto(h.app, h.uri, COUNT (DISTINCT h.ip)) FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.ip) DESC")
    List<ViewStatsDto> findByDateAndUniqueIp(LocalDateTime start, LocalDateTime end);

    /**
     * Метод получения списка посещений по времени начала и конца
     *
     * @param start Дата и время начала диапазона за который нужно выгрузить статистику
     * @param end   Дата и время конца диапазона за который нужно выгрузить статистику
     * @param uris  Список uri для которых нужно выгрузить статистику
     * @return Сформированный список статистики по посещениям
     */
    @Query("SELECT NEW ru.practicum.exp.stat.dto.ViewStatsDto(h.app, h.uri, COUNT (h.ip)) FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.ip) DESC")
    List<ViewStatsDto> findByDateAndUris(LocalDateTime start, LocalDateTime end, @Param("uris") List<String> uris);

    /**
     * Метод получения списка посещений по времени начала и конца только с уникальным ip
     *
     * @param start Дата и время начала диапазона за который нужно выгрузить статистику
     * @param end   Дата и время конца диапазона за который нужно выгрузить статистику
     * @param uris  Список uri для которых нужно выгрузить статистику
     * @return Сформированный список статистики по посещениям
     */
    @Query("SELECT NEW ru.practicum.exp.stat.dto.ViewStatsDto(h.app, h.uri, COUNT (DISTINCT h.ip)) FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.ip) DESC")
    List<ViewStatsDto> findByDateAndUrisWithUniqueIp(LocalDateTime start, LocalDateTime end, @Param("uris") List<String> uris);
}