package com.spring.myrestaurant.dto;

import lombok.Data;

@Data
public class UsernamePasswordAuthenticationRequestDto {

    private String username;
    private String password;

}
