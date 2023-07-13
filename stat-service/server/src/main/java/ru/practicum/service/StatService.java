package ru.practicum.service;

import ru.practicum.StatisticRequestDto;
import ru.practicum.StatisticResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    StatisticResponseDto createQuery(StatisticRequestDto statisticRequestDto);

    List<StatisticResponseDto> getStat(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
}