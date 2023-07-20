package ru.practicum.services;


import ru.practicum.models.dto.NewUserRequest;
import ru.practicum.models.dto.UserDto;

import java.util.List;

/**
 * Интерфейс UserAdminService для обработки логики запросов из UserAdminController
 */
public interface UserAdminService {

    /**
     * Метод получения информации о пользователях
     *
     * @param ids  ID пользователей
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return Список пользователей
     */
    List<UserDto> get(List<Long> ids, int from, int size);

    /**
     * Метод создания пользователя
     *
     * @param newUserRequest Объект нового пользователя
     * @return Созданный объект пользователя
     */
    UserDto create(NewUserRequest newUserRequest);

    /**
     * Метод удаления пользователя по ID
     *
     * @param id ID пользователя
     */
    void delete(Long id);
}