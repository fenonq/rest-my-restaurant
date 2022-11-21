package com.spring.myrestaurant.service;

import com.spring.myrestaurant.dto.DishDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface DishService extends CrudService<DishDto, Long> {

    Page<DishDto> findAll(Pageable pageable);

    DishDto changeVisibility(Long id);

}
