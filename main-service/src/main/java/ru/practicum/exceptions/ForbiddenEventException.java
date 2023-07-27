package ru.practicum.exceptions;

/**
 * Класс собственного исключения при работе с недоступным/запрещённым объектом
 */
public class ForbiddenEventException extends RuntimeException {
    public ForbiddenEventException(String message) {
        super(message);
    }
}