package com.spring.myrestaurant.mapper;

import com.spring.myrestaurant.dto.RoleDto;
import com.spring.myrestaurant.dto.StatusDto;
import com.spring.myrestaurant.model.Role;
import com.spring.myrestaurant.model.Status;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    Role mapRoleDtoToRole(RoleDto roleDto);

    RoleDto mapRoleToRoleDto(Role role);

}
