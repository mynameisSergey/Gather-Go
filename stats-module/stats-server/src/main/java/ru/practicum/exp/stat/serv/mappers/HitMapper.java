package ru.practicum.exp.stat.serv.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.exp.stat.dto.HitDto;
import ru.practicum.exp.stat.serv.models.Hit;
import ru.practicum.exp.stat.serv.util.DateFormatter;

/**
 * Класс HitMapper для преобразования HitDto в Hit
 */
@UtilityClass
public class HitMapper {

    /**
     * Статический метод преобразования HitDto в Hit
     *
     * @param hitDto объект HitDto
     * @return Преобразованный объект Hit
     */
    public static Hit toHit(HitDto hitDto) {
        return Hit.builder()
                .ip(hitDto.getIp())
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .timestamp(DateFormatter.formatDate(hitDto.getTimestamp()))
                .build();
    }
}