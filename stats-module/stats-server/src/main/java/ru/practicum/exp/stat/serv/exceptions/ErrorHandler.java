package ru.practicum.exp.stat.serv.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Класс ErrorHandler обработчик ошибок
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    /**
     * Обработчик исключений ValidationDateException
     *
     * @param e Исключение, которое нужно обработать
     * @return Сообщение об ошибке в формате Map
     */
    @ExceptionHandler(ValidationDateException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationDateException(final ValidationDateException e) {
        log.warn("400 {}", e.getMessage());
        return Map.of("error", e.getMessage());
    }
    /**
     * Обработчик всех остальных исключений
     *
     * @param e Исключение, которое нужно обработать
     * @return Сообщение об ошибке в формате Map
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGenericException(final Exception e) {
        log.error("500 {}", e.getMessage(), e);
        return Map.of("error", "Произошла ошибка на сервере: " + e.getMessage());
    }
}