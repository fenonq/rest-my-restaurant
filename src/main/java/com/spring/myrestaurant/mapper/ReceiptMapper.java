package com.spring.myrestaurant.mapper;

import com.spring.myrestaurant.dto.ReceiptDto;
import com.spring.myrestaurant.model.Receipt;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReceiptMapper {

    ReceiptMapper INSTANCE = Mappers.getMapper(ReceiptMapper.class);

    Receipt mapReceiptDtoToReceipt(ReceiptDto receiptDto);

    ReceiptDto mapReceiptToReceiptDto(Receipt receipt);

}
