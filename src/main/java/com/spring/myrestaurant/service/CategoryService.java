package com.spring.myrestaurant.service;

import com.spring.myrestaurant.dto.CategoryDto;

public interface CategoryService extends CrudService<CategoryDto, Long> {

    CategoryDto changeVisibility(Long id);

}
