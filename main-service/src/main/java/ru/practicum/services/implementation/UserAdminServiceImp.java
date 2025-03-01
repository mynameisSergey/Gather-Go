package ru.practicum.services.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictNameAndEmailException;
import ru.practicum.exceptions.model.NotFoundException;
import ru.practicum.mappers.UserMapper;
import ru.practicum.models.User;
import ru.practicum.models.dto.NewUserRequest;
import ru.practicum.models.dto.UserDto;
import ru.practicum.repositories.UserRepository;
import ru.practicum.services.UserAdminService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс UserAdminServiceImp для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAdminServiceImp implements UserAdminService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> get(List<Long> ids, int from, int size) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Параметры 'from' и 'size' должны быть положительными");
        }

        Pageable pageable = PageRequest.of(from / size, size);
        if (ids == null || ids.isEmpty()) {
            log.info("Получен запрос на получение списка пользователей без id");
            return userRepository.findAll(pageable).stream()
                    .map(UserMapper::userToDto)
                    .collect(Collectors.toList());
        } else {
            log.info("Получен запрос на получение списка пользователей по id");
            return userRepository.findByIdIn(ids, pageable).stream()
                    .map(UserMapper::userToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public UserDto create(NewUserRequest newUserRequest) {
        User user = UserMapper.newUserRequestToUser(newUserRequest);
        try {
            log.info("Получен запрос на добавление пользователя {}", newUserRequest);
            return UserMapper.userToDto(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictNameAndEmailException("E-mail: " + newUserRequest.getEmail() + " или name пользователя: " +
                    newUserRequest.getName() + " уже есть в базе");
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Запрос на добавление пользователя" + newUserRequest + " составлен неправильно");
        }
    }

    @Override
    @Transactional
    public void delete(Long id) throws NotFoundException {
        User user = userRepository.get(id);
        if (user == null) {
            log.warn("Пользователь с id: {} не найден", id);
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Получен запрос на удаление пользователя с id: {}", id);
        userRepository.delete(user);
    }
}