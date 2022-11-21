package com.spring.myrestaurant.controller.assembler;

import com.spring.myrestaurant.controller.CategoryController;
import com.spring.myrestaurant.controller.model.CategoryModel;
import com.spring.myrestaurant.dto.CategoryDto;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CategoryAssembler extends RepresentationModelAssemblerSupport<CategoryDto, CategoryModel> {

    public static final String GET_ALL_REL = "get_all_categories";
    public static final String CREATE_REL = "create_category";
    public static final String UPDATE_REL = "update_category";
    public static final String CHANGE_VISIBILITY_REL = "change_visibility_category";

    public CategoryAssembler() {
        super(CategoryController.class, CategoryModel.class);
    }

    @Override
    public CategoryModel toModel(CategoryDto entity) {
        CategoryModel categoryModel = new CategoryModel(entity);

        Link getAll = linkTo(methodOn(CategoryController.class).getAllCategories()).withRel(GET_ALL_REL);
        Link get = linkTo(methodOn(CategoryController.class).getCategory(entity.getId())).withSelfRel();
        Link create = linkTo(methodOn(CategoryController.class).createCategory(entity)).withRel(CREATE_REL);
        Link update = linkTo(methodOn(CategoryController.class).updateCategory(entity.getId(), entity))
                .withRel(UPDATE_REL);
        Link visibility = linkTo(methodOn(CategoryController.class).changeVisibility(entity.getId()))
                .withRel(CHANGE_VISIBILITY_REL);

        categoryModel.add(getAll, get, create, update, visibility);

        return categoryModel;
    }

}
