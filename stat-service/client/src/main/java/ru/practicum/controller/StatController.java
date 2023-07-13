package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatisticRequestDto;
import ru.practicum.client.StatisticClient;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping
public class StatController {
    private final StatisticClient statisticClient;

    @PostMapping("/hit")
    public ResponseEntity<Object> createQuery(@RequestBody StatisticRequestDto statisticRequestDto) {
        return statisticClient.createQuery(statisticRequestDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStat(@RequestParam(name = "start") String start,
                                          @RequestParam(name = "end") String end,
                                          @RequestParam(name = "uris", required = false) String[] uris,
                                          @RequestParam(name = "unique", required = false, defaultValue = "false") boolean unique) {
        return statisticClient.getStat(start, end, uris, unique);
    }
}