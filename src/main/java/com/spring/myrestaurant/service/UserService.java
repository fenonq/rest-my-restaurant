package com.spring.myrestaurant.service;

import com.spring.myrestaurant.dto.UserDto;

public interface UserService extends CrudService<UserDto, Long> {

    UserDto findUserByUsername(String username);

    UserDto addDishToCart(String username, Long dishId);

    UserDto removeDishFromCart(String username, Long dishId);

    UserDto changeActive(Long userId);

    UserDto changeRole(Long userId, Long roleId);

    UserDto update(String username, UserDto userDto);

}
