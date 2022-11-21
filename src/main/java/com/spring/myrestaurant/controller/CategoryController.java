package com.spring.myrestaurant.controller;

import com.spring.myrestaurant.api.CategoryApi;
import com.spring.myrestaurant.controller.assembler.CategoryAssembler;
import com.spring.myrestaurant.controller.model.CategoryModel;
import com.spring.myrestaurant.dto.CategoryDto;
import com.spring.myrestaurant.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {

    private final CategoryService categoryService;
    private final CategoryAssembler categoryAssembler;

    @Override
    public List<CategoryModel> getAllCategories() {
        log.info("find all categories");
        List<CategoryDto> outCategoryDtoList = categoryService.findAll();
        return outCategoryDtoList.stream().map(categoryAssembler::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryModel getCategory(Long id) {
        log.info("find category with id {}", id);
        CategoryDto outCategoryDto = categoryService.findById(id);
        return categoryAssembler.toModel(outCategoryDto);
    }

    @Override
    public CategoryModel createCategory(CategoryDto categoryDto) {
        log.info("save category");
        CategoryDto outCategoryDto = categoryService.save(categoryDto);
        return categoryAssembler.toModel(outCategoryDto);
    }

    @Override
    public CategoryModel updateCategory(Long id, CategoryDto categoryDto) {
        log.info("update category with id {}", id);
        CategoryDto outCategoryDto = categoryService.update(id, categoryDto);
        return categoryAssembler.toModel(outCategoryDto);
    }

    @Override
    public CategoryModel changeVisibility(Long id) {
        log.info("change category visibility with id {}", id);
        CategoryDto outCategoryDto = categoryService.changeVisibility(id);
        return categoryAssembler.toModel(outCategoryDto);
    }

}
