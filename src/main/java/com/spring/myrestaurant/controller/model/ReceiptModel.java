package com.spring.myrestaurant.controller.model;

import com.spring.myrestaurant.dto.ReceiptDto;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReceiptModel extends RepresentationModel<ReceiptModel> {

    @JsonUnwrapped
    private ReceiptDto receiptDto;

}
