package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatisticRequestDto;
import ru.practicum.StatisticResponseDto;
import ru.practicum.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping
public class StatController {
    private final StatService statService;

    @PostMapping("/hit")
    public StatisticResponseDto createStat(@RequestBody StatisticRequestDto statRequestDto) {
        return statService.createQuery(statRequestDto);
    }

    @GetMapping("/stats")
    public List<StatisticResponseDto> getStat(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                              @RequestParam(name = "uris") String[] uris,
                                              @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        return statService.getStat(start, end, uris, unique);
    }
}