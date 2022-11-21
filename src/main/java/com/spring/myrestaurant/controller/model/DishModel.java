package com.spring.myrestaurant.controller.model;

import com.spring.myrestaurant.dto.DishDto;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DishModel extends RepresentationModel<DishModel> {

    @JsonUnwrapped
    private DishDto dishDto;

}
