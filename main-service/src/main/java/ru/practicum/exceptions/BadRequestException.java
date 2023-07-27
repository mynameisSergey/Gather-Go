package ru.practicum.exceptions;

/**
 * Класс собственного исключения при работе с неправильным запросом
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}