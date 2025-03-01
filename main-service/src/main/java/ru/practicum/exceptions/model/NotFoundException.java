package ru.practicum.exceptions.model;

public class NotFoundException extends Throwable {
    public NotFoundException(String string) {
        super(string);
    }
}
