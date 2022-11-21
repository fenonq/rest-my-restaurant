package com.spring.myrestaurant.mapper;

import com.spring.myrestaurant.dto.UserDto;
import com.spring.myrestaurant.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User mapUser(UserDto userDto);

    UserDto mapUserDto(User user);

    default User populateUserWithPresentUserDtoFields(User user, UserDto userDto) {
        String firstName = userDto.getFirstName();
        if (Objects.nonNull(firstName)) {
            user.setFirstName(firstName);
        }
        String lastName = userDto.getLastName();
        if (Objects.nonNull(lastName)) {
            user.setLastName(lastName);
        }
        return user;
    }

}
