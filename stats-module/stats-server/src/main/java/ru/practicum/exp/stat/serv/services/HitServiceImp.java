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
        Hit hit = HitMapper.toHit(hitDto);
        log.info("Запрос {} ", hitDto.getUri());
        hitRepository.save(hit);
    }
}