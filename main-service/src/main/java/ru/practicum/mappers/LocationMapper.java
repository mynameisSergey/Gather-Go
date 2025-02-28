package ru.practicum.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.models.Location;
import ru.practicum.models.dto.LocationDto;

/**
 * Утилитарный класс LocationMapper для преобразования Location / LocationDto
 */
@UtilityClass
public class LocationMapper {

    /**
     * Преобразование LocationDto в Location
     *
     * @param locationDto Объект LocationDto
     * @return Преобразованный объект Location
     */
    public Location locationDtoToLocation(LocationDto locationDto) {
        if (locationDto == null)
            throw new IllegalArgumentException("LocationDto cannot be null");

        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

    /**
     * Преобразование Location в LocationDto
     *
     * @param location Объект Location
     * @return Преобразованный объект LocationDto
     */
    public LocationDto locationToLocationDto(Location location) {
        if (location == null)
            throw new IllegalArgumentException("Location cannot be null");

        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}