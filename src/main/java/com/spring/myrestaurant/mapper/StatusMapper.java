package com.spring.myrestaurant.mapper;

import com.spring.myrestaurant.dto.StatusDto;
import com.spring.myrestaurant.model.Status;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StatusMapper {

    StatusMapper INSTANCE = Mappers.getMapper(StatusMapper.class);

    Status mapStatus(StatusDto statusDto);

    StatusDto mapStatusDto(Status status);

}
