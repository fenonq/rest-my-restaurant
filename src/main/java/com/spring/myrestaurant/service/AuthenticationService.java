package com.spring.myrestaurant.service;

import com.spring.myrestaurant.dto.AuthenticationRequestDto;
import com.spring.myrestaurant.dto.TokenResponseDto;
import com.spring.myrestaurant.dto.UserDto;

public interface AuthenticationService {

    TokenResponseDto signup(UserDto userDto);

    TokenResponseDto login(AuthenticationRequestDto requestDto);

}
