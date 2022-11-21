package com.spring.myrestaurant.controller;

import com.spring.myrestaurant.api.DishApi;
import com.spring.myrestaurant.controller.assembler.DishAssembler;
import com.spring.myrestaurant.controller.model.DishModel;
import com.spring.myrestaurant.dto.DishDto;
import com.spring.myrestaurant.service.DishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DishController implements DishApi {

    private final DishService dishService;
    private final DishAssembler dishAssembler;

    @Override
    public Page<DishModel> getAllDishes(Pageable pageable) {
        log.info("find all pageable dishes {}", pageable);
        Page<DishDto> outDishDtoList = dishService.findAll(pageable);
        return new PageImpl<>(outDishDtoList.stream().map(dishAssembler::toModel)
                .collect(Collectors.toList()), pageable, outDishDtoList.getSize());
    }

    @Override
    public DishModel getDish(Long id) {
        log.info("find dish with id {}", id);
        DishDto outDishDto = dishService.findById(id);
        return dishAssembler.toModel(outDishDto);
    }

    @Override
    public DishModel createDish(DishDto dishDto) {
        log.info("save dish");
        DishDto outDishDto = dishService.save(dishDto);
        return dishAssembler.toModel(outDishDto);
    }

    @Override
    public DishModel updateDish(Long id, DishDto dishDto) {
        log.info("update dish with id {}", id);
        DishDto outDishDto = dishService.update(id, dishDto);
        return dishAssembler.toModel(outDishDto);
    }

    @Override
    public DishModel changeVisibility(Long id) {
        log.info("change dish visibility with id {}", id);
        DishDto outDishDto = dishService.changeVisibility(id);
        return dishAssembler.toModel(outDishDto);
    }

}
