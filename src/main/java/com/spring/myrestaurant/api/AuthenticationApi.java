package com.spring.myrestaurant.api;

import com.spring.myrestaurant.dto.AuthenticationRequestDto;
import com.spring.myrestaurant.dto.TokenResponseDto;
import com.spring.myrestaurant.dto.UserDto;
import com.spring.myrestaurant.dto.group.OnCreate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Api(tags = "Authentication management api")
@RequestMapping("/api/v1/authentication")
public interface AuthenticationApi {

    @ApiOperation("Signup")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    TokenResponseDto signup(@RequestBody @Validated(OnCreate.class) UserDto userDto);

    @ApiOperation("Login")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/login")
    TokenResponseDto login(@RequestBody AuthenticationRequestDto usernameAndPassword);

}
