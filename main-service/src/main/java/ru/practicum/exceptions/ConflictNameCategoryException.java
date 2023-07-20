package ru.practicum.exceptions;

/**
 * Класс собственного исключения при работе с данными категории
 */
public class ConflictNameCategoryException extends RuntimeException {
    public ConflictNameCategoryException(String message) {
        super(message);
    }
}