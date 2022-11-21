package com.spring.myrestaurant.controller.assembler;

import com.spring.myrestaurant.controller.ReceiptController;
import com.spring.myrestaurant.controller.model.ReceiptModel;
import com.spring.myrestaurant.dto.ReceiptDto;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ReceiptAssembler extends RepresentationModelAssemblerSupport<ReceiptDto, ReceiptModel> {

    public static final String GET_ALL_REL = "get_all_receipts";
    public static final String GET_USER_REL = "get_user_receipts";
    public static final String CREATE_REL = "create_receipt";
    public static final String NEXT_STATUS_REL = "next_status_receipt";
    public static final String CANCEL_RENEW_REL = "cancel_renew_receipt";

    public ReceiptAssembler() {
        super(ReceiptController.class, ReceiptModel.class);
    }

    @Override
    public ReceiptModel toModel(ReceiptDto entity) {
        ReceiptModel receiptModel = new ReceiptModel(entity);

        Link getAll = linkTo(methodOn(ReceiptController.class).getAllReceipts()).withRel(GET_ALL_REL);
        Link getUser = linkTo(methodOn(ReceiptController.class)
                .getUserReceipts(null)).withRel(GET_USER_REL);
        Link create = linkTo(methodOn(ReceiptController.class).makeOrder(null))
                .withRel(CREATE_REL);
        Link next = linkTo(methodOn(ReceiptController.class).nextStatus(entity.getId(),
                null)).withRel(NEXT_STATUS_REL);
        Link cancel = linkTo(methodOn(ReceiptController.class).cancelOrRenewReceipt(entity.getId(),
                null)).withRel(CANCEL_RENEW_REL);

        receiptModel.add(getAll, getUser, create, next, cancel);

        return receiptModel;
    }

}
