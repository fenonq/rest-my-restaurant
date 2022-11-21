package com.spring.myrestaurant.dto;

import lombok.Data;

@Data
public class UsernameAndPasswordAuthenticationRequestDto {

    private String username;
    private String password;

}
