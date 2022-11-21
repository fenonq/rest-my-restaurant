package com.spring.myrestaurant.controller;

import com.spring.myrestaurant.api.ReceiptApi;
import com.spring.myrestaurant.controller.assembler.ReceiptAssembler;
import com.spring.myrestaurant.controller.model.ReceiptModel;
import com.spring.myrestaurant.dto.ReceiptDto;
import com.spring.myrestaurant.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReceiptController implements ReceiptApi {

    private final ReceiptService receiptService;
    private final ReceiptAssembler receiptAssembler;

    @Override
    public List<ReceiptModel> getAllReceipts() {
        log.info("find all receipts");
        List<ReceiptDto> outReceiptDtoList = receiptService.findAll();
        return outReceiptDtoList.stream().map(receiptAssembler::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReceiptModel> getUserReceipts(Authentication authentication) {
        log.info("find all user receipts");
        List<ReceiptDto> outReceiptDtoList = receiptService.findByCustomerUsername(authentication.getName());
        return outReceiptDtoList.stream().map(receiptAssembler::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public ReceiptModel makeOrder(Authentication authentication) {
        log.info("make an order");
        ReceiptDto outReceiptDto = receiptService.makeOrder(authentication.getName());
        return receiptAssembler.toModel(outReceiptDto);
    }

    @Override
    public ReceiptModel nextStatus(Long receiptId, Authentication authentication) {
        log.info("change status in receipt with id {}", receiptId);
        ReceiptDto outReceiptDto = receiptService.nextStatus(receiptId, authentication.getName());
        return receiptAssembler.toModel(outReceiptDto);
    }

    @Override
    public ReceiptModel cancelOrRenewReceipt(Long receiptId, Authentication authentication) {
        log.info("cancel/renew receipt with id {}", receiptId);
        ReceiptDto outReceiptDto = receiptService.cancelOrRenewReceipt(receiptId, authentication.getName());
        return receiptAssembler.toModel(outReceiptDto);
    }

}
