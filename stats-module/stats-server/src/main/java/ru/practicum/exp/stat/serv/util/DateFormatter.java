package ru.practicum.exp.stat.serv.util;

import lombok.experimental.UtilityClass;
import ru.practicum.exp.stat.serv.exceptions.ValidationDateException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Утилитарный класс для валидации работы с датой и временем
 */
@UtilityClass
public class DateFormatter {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    /**
     * Статический метод проверки и преобразования даты и времени
     *
     * @param date Входной параметр даты и времени в виде String
     * @return Преобразованная дата и время в LocalDateTime
     */
    public static LocalDateTime formatDate(String date) {
        if (isInvalidDate(date)) {
            throw new ValidationDateException("Дата должна быть задана");
        }
        try {
            return LocalDateTime.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new ValidationDateException("Неверный формат даты");
        }
    }

    private boolean isInvalidDate(String date) {
        return date == null || date.isBlank();
    }
}