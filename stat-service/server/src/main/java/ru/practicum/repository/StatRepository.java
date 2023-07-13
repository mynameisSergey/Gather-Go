package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Stat;
import ru.practicum.model.StatResult;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Stat, Long> {
    @Query(nativeQuery = true, value = "SELECT a.uri,a.app, count(Distinct(a.ip)) hit " +
            "FROM  stats a where a.uri in :uris and a.timestamp between :start and :end " +
            "GROUP BY a.uri, a.app")
    List<StatResult> findDistinctAndTimeStampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(nativeQuery = true, value = "SELECT a.uri,a.app, count(Distinct(a.ip)) hit " +
            "FROM  stats a where  a.timestamp between :start and :end " +
            "GROUP BY a.uri, a.app")
    List<StatResult> findDistinctAndTimeStampBetween(LocalDateTime start, LocalDateTime end);

    @Query(nativeQuery = true, value = "SELECT a.uri,a.app, count( a.ip) hit  " +
            "FROM  stats a where a.uri in :uris and a.timestamp between :start and :end " +
            "GROUP BY a.uri, a.app")
    List<StatResult> findAndTimeStampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(nativeQuery = true, value = "SELECT a.uri,a.app, count( a.ip) hit " +
            "FROM  stats a where a.timestamp between :start and :end " +
            "GROUP BY a.uri, a.app")
    List<StatResult> findAndTimeStampBetween(LocalDateTime start, LocalDateTime end);
}