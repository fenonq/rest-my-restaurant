package com.spring.myrestaurant.api;

import com.spring.myrestaurant.controller.model.UserModel;
import com.spring.myrestaurant.dto.UserDto;
import com.spring.myrestaurant.dto.UsernamePasswordAuthenticationRequestDto;
import com.spring.myrestaurant.dto.group.OnCreate;
import com.spring.myrestaurant.dto.group.OnUpdate;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "User management api")
@RequestMapping("/api/v1/users")
public interface UserApi {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<UserModel> getAllUsers();

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", required = true, value = "User id")
    })
    @ApiOperation("Get user by id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    UserModel getUser(@PathVariable Long id);

    @ApiOperation("Create user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    UserModel createUser(@RequestBody @Validated(OnCreate.class) UserDto userDto);

    @ApiOperation("Login")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/login")
    default void login(@RequestBody UsernamePasswordAuthenticationRequestDto usernameAndPassword) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

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
