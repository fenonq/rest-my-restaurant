package com.spring.myrestaurant.controller;

import com.spring.myrestaurant.api.UserApi;
import com.spring.myrestaurant.controller.assembler.UserAssembler;
import com.spring.myrestaurant.controller.model.UserModel;
import com.spring.myrestaurant.dto.UserDto;
import com.spring.myrestaurant.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;
    private final UserAssembler userAssembler;

    @Override
    public List<UserModel> getAllUsers() {
        log.info("find all users");
        List<UserDto> outUserDtoList = userService.findAll();
        return outUserDtoList.stream().map(userAssembler::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public UserModel getUser(Long id) {
        log.info("find user with id {}", id);
        UserDto outUserDto = userService.findById(id);
        return userAssembler.toModel(outUserDto);
    }

    @Override
    public UserModel createUser(UserDto userDto) {
        log.info("save user");
        UserDto outUserDto = userService.save(userDto);
        return userAssembler.toModel(outUserDto);
    }

    @Override
    public UserModel updateUser(UserDto userDto, Authentication authentication) {
        log.info("update user");
        UserDto outUserDto = userService.update(authentication.getName(), userDto);
        return userAssembler.toModel(outUserDto);
    }

    @Override
    public UserModel addDishToCart(Long dishId, Authentication authentication) {
        log.info("adding dish {} to user cart", dishId);
        UserDto outUserDto = userService.addDishToCart(authentication.getName(), dishId);
        return userAssembler.toModel(outUserDto);
    }

    @Override
    public UserModel removeDishFromCart(Long dishId, Authentication authentication) {
        log.info("removing dish {} to user cart", dishId);
        UserDto outUserDto = userService.removeDishFromCart(authentication.getName(), dishId);
        return userAssembler.toModel(outUserDto);
    }

    @Override
    public UserModel banUser(Long id) {
        log.info("ban/unban user with id {}", id);
        UserDto outUserDto = userService.changeActive(id);
        return userAssembler.toModel(outUserDto);
    }

    @Override
    public UserModel changeRole(Long userId, Long roleId) {
        log.info("change user {} role to role {}", userId, roleId);
        UserDto outUserDto = userService.changeRole(userId, roleId);
        return userAssembler.toModel(outUserDto);
    }

}
