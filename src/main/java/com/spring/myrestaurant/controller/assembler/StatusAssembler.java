package com.spring.myrestaurant.controller.assembler;

import com.spring.myrestaurant.controller.StatusController;
import com.spring.myrestaurant.controller.model.StatusModel;
import com.spring.myrestaurant.dto.StatusDto;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class StatusAssembler extends RepresentationModelAssemblerSupport<StatusDto, StatusModel> {

    public static final String GET_ALL_REL = "get_all_statuses";
    public static final String CREATE_REL = "create_status";
    public static final String UPDATE_REL = "update_status";

    public StatusAssembler() {
        super(StatusController.class, StatusModel.class);
    }

    @Override
    public StatusModel toModel(StatusDto entity) {
        StatusModel statusModel = new StatusModel(entity);

        Link getAll = linkTo(methodOn(StatusController.class).getAllStatuses()).withRel(GET_ALL_REL);
        Link get = linkTo(methodOn(StatusController.class).getStatus(entity.getId())).withSelfRel();
        Link create = linkTo(methodOn(StatusController.class).createStatus(entity)).withRel(CREATE_REL);
        Link update = linkTo(methodOn(StatusController.class).updateStatus(entity.getId(), entity))
                .withRel(UPDATE_REL);

        statusModel.add(getAll, get, create, update);

        return statusModel;
    }

}
