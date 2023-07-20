package ru.practicum.exp.stat.serv.exceptions;

/**
 * Класс собственного исключения при работе с датой и временем
 */
public class ValidationDateException extends RuntimeException {
    public ValidationDateException(String message) {
        super(message);
    }
}