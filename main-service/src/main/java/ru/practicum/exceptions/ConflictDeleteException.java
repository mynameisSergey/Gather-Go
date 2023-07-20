package ru.practicum.exceptions;

/**
 * Класс собственного исключения при удалении категории связанной событием
 */
public class ConflictDeleteException extends RuntimeException {
    public ConflictDeleteException(String message) {
        super(message);
    }
}