package ru.practicum.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.ResourceNotFoundException;
import ru.practicum.models.*;

import java.util.List;

/**
 * Класс поиска объекта в БД
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindObjectInRepository {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CompilationRepository compilationRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    /**
     * Метод проверки категории в базе по ID
     *
     * @param id ID категории
     * @return Искомая категория
     */
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Категория c id: " + id + " не найдена"));
    }

    /**
     * Метод проверки пользователя в базе по ID
     *
     * @param id ID пользователя
     * @return Искомый пользователь
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Пользователь c id: " + id + " не найден"));
    }

    /**
     * Метод проверки подборки события в базе по ID
     *
     * @param id ID подборки события
     * @return Искомое подборка события
     */
    public Compilation getCompilationById(Long id) {
        return compilationRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Подборка событий c id = " + id + " не найдена"));
    }

    /**
     * Метод проверки события в базе по ID
     *
     * @param id ID события
     * @return Искомое событие
     */
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Событие c id = " + id + " не найдено"));
    }

    /**
     * Метод проверки запроса в базе по ID
     *
     * @param id ID запроса
     * @return Искомый запрос
     */
    public Request getRequestById(Long id) {
        return requestRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Запрос на участие c id = " + id + " не найден"));
    }

    /**
     * Метод проверки Категории в базе по объекту
     *
     * @param category Искомый объект категории
     * @return Найдена или нет искомая категория (true/false)
     */
    public boolean isRelatedEvent(Category category) {
        List<Event> findEventByCategory = eventRepository.findEventByCategoryIs(category);
        return !findEventByCategory.isEmpty();
    }
}