package com.spring.myrestaurant.service.impl;

import com.spring.myrestaurant.dto.DishDto;
import com.spring.myrestaurant.exception.EntityNotFoundException;
import com.spring.myrestaurant.mapper.DishMapper;
import com.spring.myrestaurant.model.Dish;
import com.spring.myrestaurant.repository.DishRepository;
import com.spring.myrestaurant.service.DishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;

    @Override
    public List<DishDto> findAll() {
        log.info("find all dishes");
        return dishRepository.findAll()
                .stream()
                .map(DishMapper.INSTANCE::mapDishToDishDto)
                .collect(Collectors.toList());
    }

    @Override
    public DishDto findById(Long id) {
        log.info("find dish with id {}", id);
        Dish dish = dishRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return DishMapper.INSTANCE.mapDishToDishDto(dish);
    }

    @Override
    public DishDto save(DishDto dishDto) {
        log.info("save dish");
        Dish dish = DishMapper.INSTANCE.mapDishDtoToDish(dishDto);
        dish.setVisible(true);
        dish = dishRepository.save(dish);
        return DishMapper.INSTANCE.mapDishToDishDto(dish);
    }

    @Override
    @Transactional
    public DishDto update(Long id, DishDto dishDto) {
        log.info("update dish with id {}", id);
        Dish existingDish = dishRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Dish dish = DishMapper.INSTANCE.mapDishDtoToDish(dishDto);
        dish.setVisible(existingDish.isVisible());
        dish = dishRepository.save(dish);
        return DishMapper.INSTANCE.mapDishToDishDto(dish);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("delete dish with id {}", id);
        Dish dish = dishRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        dishRepository.delete(dish);
    }

    @Override
    public Page<DishDto> findAllByVisible(Pageable pageable) {
        log.info("find all dishes {}", pageable);
        Page<Dish> pagedResult = dishRepository.findAllByVisible(pageable, Boolean.TRUE);
        return new PageImpl<>(pagedResult.getContent().stream().map(DishMapper.INSTANCE::mapDishToDishDto)
                .collect(Collectors.toList()), pageable, pagedResult.getSize());
    }

    @Override
    @Transactional
    public DishDto changeVisibility(Long id) {
        log.info("change dish visibility with id {}", id);
        Dish dish = dishRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        dish.setVisible(!dish.isVisible());
        dishRepository.save(dish);
        return DishMapper.INSTANCE.mapDishToDishDto(dish);
    }

}
