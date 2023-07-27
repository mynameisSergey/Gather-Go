package ru.practicum.exceptions;

/**
 * Класс собственного исключения при работе с данными пользователя Name / E-Mail
 */
public class ConflictNameAndEmailException extends RuntimeException {
    public ConflictNameAndEmailException(String message) {
        super(message);
    }
}