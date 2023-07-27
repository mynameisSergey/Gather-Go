package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.models.dto.CompilationDto;
import ru.practicum.models.dto.NewCompilationDto;
import ru.practicum.models.dto.UpdateCompilationRequest;
import ru.practicum.services.CompilationAdminService;


/**
 * Класс CompilationAdminController по энпоинту admin/compilations
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class CompilationAdminController {
    private final CompilationAdminService compilationAdminService;


    /**
     * Метод (эндпоинт) создания подборки
     *
     * @param newCompilationDto Объект NewCompilationDto
     * @return Созданный объект подборки NewCompilationDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Validated @RequestBody NewCompilationDto newCompilationDto) {
        return compilationAdminService.create(newCompilationDto);
    }

    /**
     * Метод (эндпоинт) обновления подборки по ID
     *
     * @param compId                   ID подборки
     * @param updateCompilationRequest Объект подборки UpdateCompilationRequest
     * @return Изменённый объект подборки CompilationDto
     */
    @PatchMapping("/{compId}")
    public CompilationDto update(@PathVariable Long compId,
                                 @Validated @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        return compilationAdminService.update(compId, updateCompilationRequest);
    }

    /**
     * Метод (эндпоинт) удаления подборки по ID
     *
     * @param compId ID подборки
     */
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        compilationAdminService.delete(compId);
    }
}