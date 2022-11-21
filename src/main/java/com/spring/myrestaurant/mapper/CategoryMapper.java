package com.spring.myrestaurant.mapper;

import com.spring.myrestaurant.dto.CategoryDto;
import com.spring.myrestaurant.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category mapCategory(CategoryDto categoryDto);

    CategoryDto mapCategoryDto(Category category);

}
