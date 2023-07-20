package ru.practicum.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.models.Compilation;
import ru.practicum.models.Event;
import ru.practicum.models.dto.CompilationDto;
import ru.practicum.models.dto.NewCompilationDto;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Утилитарный класс CompilationMapper для преобразования Compilation / newCompilationDto / CompilationDto
 */
@UtilityClass
public class CompilationMapper {

    /**
     * Преобразование NewCompilationDto в Compilation
     *
     * @param newCompilationDto Объект NewCompilationDto
     * @return Преобразованный объект Compilation
     */
    public Compilation newCompilationDtoToCompilationAndEvents(NewCompilationDto newCompilationDto, Set<Event> events) {
        return Compilation.builder()
                .events(events)
                .pinned(newCompilationDto.isPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    /**
     * Преобразование Compilation в CompilationDto
     *
     * @param compilation Объект Compilation
     * @return Преобразованный объект CompilationDto
     */
    public CompilationDto compilationToCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(compilation.getEvents()
                        .stream().map(EventMapper::eventToeventShortDto).collect(Collectors.toList()))
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .build();
    }
}