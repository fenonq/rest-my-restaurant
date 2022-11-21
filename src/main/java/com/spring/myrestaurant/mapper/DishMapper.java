package com.spring.myrestaurant.mapper;

import com.spring.myrestaurant.dto.DishDto;
import com.spring.myrestaurant.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DishMapper {

    DishMapper INSTANCE = Mappers.getMapper(DishMapper.class);

    Dish mapDish(DishDto dishDto);

    DishDto mapDishDto(Dish dish);

}
