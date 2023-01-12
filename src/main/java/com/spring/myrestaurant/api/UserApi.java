package com.spring.myrestaurant.api;

import com.spring.myrestaurant.controller.model.UserModel;
import com.spring.myrestaurant.dto.UserDto;
import com.spring.myrestaurant.dto.group.OnUpdate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

@Api(tags = "User management api")
@RequestMapping("/api/v1/users")
public interface UserApi {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<UserModel> getAllUsers();

    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", paramType = "path", required = true, value = "User username")
    })
    @ApiOperation("Get user by username")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{username}")
    UserModel getUser(@PathVariable String username);


    @ApiOperation("Update user firstName and lastName")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    UserModel updateUser(@RequestBody @Validated(OnUpdate.class) UserDto userDto,
                         @ApiIgnore Authentication authentication);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "dishId", paramType = "path", required = true, value = "Dish id")
    })
    @ApiOperation("Add dish to user cart")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/cart/add/{dishId}")
    UserModel addDishToCart(@PathVariable Long dishId, @ApiIgnore Authentication authentication);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "dishId", paramType = "path", required = true, value = "Dish id")
    })
    @ApiOperation("Remove dish to user cart")
    @PatchMapping(value = "/cart/remove/{dishId}")
    UserModel removeDishFromCart(@PathVariable Long dishId, @ApiIgnore Authentication authentication);

    @ApiOperation("Get user cart")
    @GetMapping(value = "/cart")
    Map<Object, Long> getUserCart(@ApiIgnore Authentication authentication);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", required = true, value = "User id")
    })
    @ApiOperation("Ban/Unban user by id")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/ban/{id}")
    UserModel banUser(@PathVariable Long id);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "path", required = true, value = "User id"),
            @ApiImplicitParam(name = "roleId", paramType = "path", required = true, value = "Role id")
    })
    @ApiOperation("Change user role")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{userId}/change-role/{roleId}")
    UserModel changeRole(@PathVariable Long userId, @PathVariable Long roleId);

}
