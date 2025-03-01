package ru.practicum.exp.stat.serv.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exp.stat.dto.HitDto;
import ru.practicum.exp.stat.serv.mappers.HitMapper;
import ru.practicum.exp.stat.serv.models.Hit;
import ru.practicum.exp.stat.serv.repositories.HitRepository;

/**
 * Класс HitServiceImp для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HitServiceImp implements HitService {

    private final HitRepository hitRepository;

    @Override
    @Transactional
    public void create(HitDto hitDto) {
        if (hitDto == null) {
            log.warn("HitDto is null");
            return; // Или выбросьте исключение, если это критично
        }
        Hit hit = HitMapper.toHit(hitDto);
        log.info("Информация о запросе {}", hitDto.getUri());
        try {
            hitRepository.save(hit);
        } catch (Exception e) {
            log.error("Ошибка при сохранении хита: {}", e.getMessage());
            // Можно выбросить пользовательское исключение или обработать ошибку по-другому
        }
    }
}