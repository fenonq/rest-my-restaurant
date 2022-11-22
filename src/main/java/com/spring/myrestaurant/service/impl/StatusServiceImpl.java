package com.spring.myrestaurant.service.impl;

import com.spring.myrestaurant.dto.StatusDto;
import com.spring.myrestaurant.exception.EntityNotFoundException;
import com.spring.myrestaurant.mapper.StatusMapper;
import com.spring.myrestaurant.model.Status;
import com.spring.myrestaurant.repository.StatusRepository;
import com.spring.myrestaurant.service.StatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Override
    public List<StatusDto> findAll() {
        log.info("find all statuses");
        return statusRepository.findAll()
                .stream()
                .map(StatusMapper.INSTANCE::mapStatusToStatusDto)
                .collect(Collectors.toList());
    }

    @Override
    public StatusDto findById(Long id) {
        log.info("find status with id {}", id);
        Status status = statusRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return StatusMapper.INSTANCE.mapStatusToStatusDto(status);
    }

    @Override
    public StatusDto save(StatusDto statusDto) {
        log.info("save status");
        Status status = StatusMapper.INSTANCE.mapStatusDtoToStatus(statusDto);
        status = statusRepository.save(status);
        return StatusMapper.INSTANCE.mapStatusToStatusDto(status);
    }

    @Override
    @Transactional
    public StatusDto update(Long id, StatusDto statusDto) {
        log.info("update status with id {}", id);
        statusRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Status status = StatusMapper.INSTANCE.mapStatusDtoToStatus(statusDto);
        status = statusRepository.save(status);
        return StatusMapper.INSTANCE.mapStatusToStatusDto(status);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("delete status with id {}", id);
        Status status = statusRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        statusRepository.delete(status);
    }

}
