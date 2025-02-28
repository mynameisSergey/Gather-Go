package ru.practicum.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.models.dto.NewUserRequest;
import ru.practicum.models.dto.UserDto;
import ru.practicum.services.UserAdminService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Класс UserAdminController по энпоинту admin/users
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserAdminController {
    private final UserAdminService userAdminService;

    /**
     * Метод (эндпоинт) получения информации о пользователях
     *
     * @param ids  ID пользователей
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return Список пользователей
     */
    @GetMapping
    List<UserDto> get(@RequestParam(required = false) List<Long> ids,
                      @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                      @Positive @RequestParam(defaultValue = "10") Integer size) {
        return userAdminService.get(ids, from, size);
    }

    /**
     * Метод (эндпоинт) создания пользователя
     *
     * @param newUserRequest Объект нового пользователя
     * @return Созданный объект пользователя
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserDto create(@Validated @RequestBody NewUserRequest newUserRequest) {
        return userAdminService.create(newUserRequest);
    }

    /**
     * Метод (эндпоинт) удаления пользователя по ID
     *
     * @param userId ID пользователя
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long userId) {
        userAdminService.delete(userId);
    }

}