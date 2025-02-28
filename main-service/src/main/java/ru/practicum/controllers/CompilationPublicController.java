package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.models.dto.CompilationDto;
import ru.practicum.services.CompilationPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Класс CompilationPublicController по энпоинту compilations
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationPublicController {

    private final CompilationPublicService compilationPublicService;

    /**
     * Метод (эндпоинт) получения списка подборок событий
     *
     * @param pinned Закреплена ли подборка на главной странице сайта
     * @param from   Количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size   Количество элементов в наборе
     * @return Список подборок событий
     */
    @GetMapping()
    public List<CompilationDto> get(@RequestParam(required = false) Boolean pinned,
                                    @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                    @Positive @RequestParam(defaultValue = "10") Integer size) {
        return compilationPublicService.get(pinned, from, size);
    }

    /**
     * Метод (эндпоинт) получения подборки событий по ID
     *
     * @param compId ID подборки событий
     * @return Искомая подборка событий
     */
    @GetMapping("/{compId}")
    public CompilationDto get(@PathVariable Long compId) {
        return compilationPublicService.get(compId);
    }
}