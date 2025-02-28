package ru.practicum.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.models.User;
import ru.practicum.models.dto.NewUserRequest;
import ru.practicum.models.dto.UserDto;
import ru.practicum.models.dto.UserShortDto;

/**
 * Утилитарный класс UserMapper для преобразования User / UserDto / UserShortDto / NewUserRequest
 */
@UtilityClass
public class UserMapper {

    /**
     * Преобразование User в UserDto
     *
     * @param user Объект User
     * @return Преобразованный объект UserDto
     */
    public UserDto userToDto(User user) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    /**
     * Преобразование User в UserShortDto
     *
     * @param user Объект User
     * @return Преобразованный объект UserShortDto
     */
    public UserShortDto userToUserShortDto(User user) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");

        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    /**
     * Преобразование NewUserRequest в User
     *
     * @param newUserRequest Объект NewUserRequest
     * @return Преобразованный объект User
     */
    public User newUserRequestToUser(NewUserRequest newUserRequest) {
        if (newUserRequest == null)
            throw new IllegalArgumentException("NewUserRequest cannot be null");

        return User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
    }
}