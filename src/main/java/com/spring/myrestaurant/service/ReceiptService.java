package com.spring.myrestaurant.service;

import com.spring.myrestaurant.dto.ReceiptDto;

import java.util.List;

public interface ReceiptService extends CrudService<ReceiptDto, Long> {

    ReceiptDto makeOrder(String username);

    ReceiptDto nextStatus(Long receiptId, String username);

    ReceiptDto cancelOrRenewReceipt(Long receiptId, String username);

    List<ReceiptDto> findByCustomerUsername(String username);

}
