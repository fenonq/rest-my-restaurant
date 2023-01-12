package com.spring.myrestaurant.controller;

import com.spring.myrestaurant.api.AuthenticationApi;
import com.spring.myrestaurant.dto.AuthenticationRequestDto;
import com.spring.myrestaurant.dto.TokenResponseDto;
import com.spring.myrestaurant.dto.UserDto;
import com.spring.myrestaurant.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationApi {

    private final AuthenticationService authenticationService;

    @Override
    public TokenResponseDto signup(UserDto userDto) {
        log.info("signup user with username {}", userDto.getUsername());
        return authenticationService.signup(userDto);
    }

    @Override
    public TokenResponseDto login(AuthenticationRequestDto usernameAndPassword) {
        log.info("login user with username {}", usernameAndPassword.getUsername());
        return authenticationService.login(usernameAndPassword);
    }

}
