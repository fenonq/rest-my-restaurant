package com.spring.myrestaurant.controller.assembler;

import com.spring.myrestaurant.controller.DishController;
import com.spring.myrestaurant.controller.model.DishModel;
import com.spring.myrestaurant.dto.DishDto;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DishAssembler extends RepresentationModelAssemblerSupport<DishDto, DishModel> {

    public static final String GET_ALL_REL = "get_all_dishes";
    public static final String GET_ALL_PAGEABLE_REL = "get_all_pageable_dishes";
    public static final String CREATE_REL = "create_dish";
    public static final String UPDATE_REL = "update_dish";
    public static final String CHANGE_VISIBILITY_REL = "change_visibility_dish";

    public DishAssembler() {
        super(DishController.class, DishModel.class);
    }

    @Override
    public DishModel toModel(DishDto entity) {
        DishModel dishModel = new DishModel(entity);

        Link getAllPageable = linkTo(methodOn(DishController.class).getAllPageableDishes(Pageable.unpaged()))
                .withRel(GET_ALL_PAGEABLE_REL);
        Link get = linkTo(methodOn(DishController.class).getDish(entity.getId())).withSelfRel();
        Link create = linkTo(methodOn(DishController.class).createDish(entity)).withRel(CREATE_REL);
        Link update = linkTo(methodOn(DishController.class).updateDish(entity.getId(), entity))
                .withRel(UPDATE_REL);
        Link visibility = linkTo(methodOn(DishController.class).changeVisibility(entity.getId()))
                .withRel(CHANGE_VISIBILITY_REL);

        dishModel.add(getAllPageable, get, create, update, visibility);

        return dishModel;
    }

}
