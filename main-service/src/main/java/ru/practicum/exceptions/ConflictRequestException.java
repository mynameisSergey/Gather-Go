package ru.practicum.exceptions;

/**
 * Класс собственного исключения при работе с запросами
 */
public class ConflictRequestException extends RuntimeException {
    public ConflictRequestException(String message) {
        super(message);
    }
}