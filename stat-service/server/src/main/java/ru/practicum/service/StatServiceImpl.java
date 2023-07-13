package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatisticRequestDto;
import ru.practicum.StatisticResponseDto;
import ru.practicum.mapper.StatMapper;
import ru.practicum.model.Stat;
import ru.practicum.model.StatResult;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    @Override
    @Transactional
    public StatisticResponseDto createQuery(StatisticRequestDto statisticRequestDto) {
        Stat stats = StatMapper.toStat(statisticRequestDto);
        Stat createdStat = statRepository.save(stats);
        return StatMapper.toStatDto(createdStat);
    }

    @Override
    public List<StatisticResponseDto> getStat(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        List<StatResult> result;
        if (uris == null || uris.length == 0) {
            if (unique) {
                result = statRepository.findDistinctAndTimeStampBetween(start, end);
            } else {
                result = statRepository.findAndTimeStampBetween(start, end);
            }
        } else {
            if (unique) {
                result = statRepository.findDistinctAndTimeStampBetweenAndUriIn(start,
                        end, Arrays.stream(uris).collect(Collectors.toList()));
            } else {
                result = statRepository.findAndTimeStampBetweenAndUriIn(start,
                        end, Arrays.stream(uris).collect(Collectors.toList()));
            }
        }
        return result.stream()
                .map(a -> new StatisticResponseDto(a.getApp(), a.getUri(), a.getHit()))
                .sorted(Comparator.comparingLong(StatisticResponseDto::getHits).reversed())
                .collect(Collectors.toList());
    }
}