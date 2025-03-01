package ru.practicum.exp.stat.serv.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exp.stat.dto.ViewStatsDto;
import ru.practicum.exp.stat.serv.exceptions.ValidationDateException;
import ru.practicum.exp.stat.serv.repositories.StatsRepository;
import ru.practicum.exp.stat.serv.util.DateFormatter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс StatServiceImp для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatServiceImp implements StatService {
    public static final String LOG_TEXT = "Получение информации о запросе start:{}, end {}";
    private final StatsRepository statsRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ViewStatsDto> get(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime newStart = DateFormatter.formatDate(start);
        LocalDateTime newEnd = DateFormatter.formatDate(end);
        if (newEnd.isBefore(newStart) || newStart.isAfter(newEnd)) {
            throw new ValidationDateException("Неверно заданы даты для поиска");
        }
        log.info(LOG_TEXT, start, end);

        if (uris == null || uris.isEmpty()) {
            return unique ? statsRepository.findByDateAndUniqueIp(newStart, newEnd) : statsRepository.findByDate(newStart, newEnd);
        } else {
            return unique ? statsRepository.findByDateAndUrisWithUniqueIp(newStart, newEnd, uris)
                    : statsRepository.findByDateAndUris(newStart, newEnd, uris);
        }
    }
}