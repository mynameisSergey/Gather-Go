package ru.practicum.services.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mappers.CompilationMapper;
import ru.practicum.models.Compilation;
import ru.practicum.models.Event;
import ru.practicum.models.dto.CompilationDto;
import ru.practicum.models.dto.NewCompilationDto;
import ru.practicum.models.dto.UpdateCompilationRequest;
import ru.practicum.repositories.CompilationRepository;
import ru.practicum.repositories.EventRepository;
import ru.practicum.services.CompilationAdminService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Класс CompilationAdminServiceImp для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationAdminServiceImp implements CompilationAdminService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Set<Event> events = new HashSet<>();
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            events = addEvents(newCompilationDto.getEvents());
        }
        Compilation compilation = CompilationMapper.newCompilationDtoToCompilationAndEvents(newCompilationDto, events);
        log.info("Получен запрос на добавление подборки событий: {}", newCompilationDto.getTitle());
        return CompilationMapper.compilationToCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation newCompilation = compilationRepository.get(compId);
        Set<Event> events;
        if (updateCompilationRequest.getEvents() != null) {
            events = addEvents(updateCompilationRequest.getEvents());
            newCompilation.setEvents(events);
        }
        if (updateCompilationRequest.getPinned() != null) {
            newCompilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null && updateCompilationRequest.getTitle().isBlank()) {
            newCompilation.setTitle(updateCompilationRequest.getTitle());
        }
        log.info("Получен запрос на обновление подборки событий по id: {}", compId);
        return CompilationMapper.compilationToCompilationDto(compilationRepository.save(newCompilation));
    }

    @Override
    @Transactional
    public void delete(Long compId) {
        compilationRepository.existsById(compId);
        log.info("Получен запрос на удаление подборки событий по id: {}", compId);
        compilationRepository.deleteById(compId);
    }

    /**
     * Метод поиска и добавления событий в подборку
     *
     * @param eventsIds Список ID событий
     * @return Список событий
     */
    private Set<Event> addEvents(List<Long> eventsIds) {
        return eventRepository.findAllByIdIsIn(eventsIds);
    }
}