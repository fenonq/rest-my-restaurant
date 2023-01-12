package com.spring.myrestaurant.service.impl;

import com.spring.myrestaurant.dto.AuthenticationRequestDto;
import com.spring.myrestaurant.dto.TokenResponseDto;
import com.spring.myrestaurant.dto.UserDto;
import com.spring.myrestaurant.exception.EntityExistsException;
import com.spring.myrestaurant.exception.EntityNotFoundException;
import com.spring.myrestaurant.jwt.JwtService;
import com.spring.myrestaurant.model.User;
import com.spring.myrestaurant.repository.RoleRepository;
import com.spring.myrestaurant.repository.UserRepository;
import com.spring.myrestaurant.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public TokenResponseDto signup(UserDto userDto) {
        log.info("register user with username {}", userDto.getUsername());
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new EntityExistsException("User with this username already exists");
        }
        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .password(new BCryptPasswordEncoder().encode(userDto.getPassword()))
                .roles(new ArrayList<>(List.of(roleRepository.findByName("ROLE_USER"))))
                .active(Boolean.TRUE)
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return TokenResponseDto.builder()
                .accessToken(token)
                .build();
    }

    @Override
    public TokenResponseDto login(AuthenticationRequestDto requestDto) {
        log.info("login user with username {}", requestDto.getUsername());
        User user = userRepository.findByUsername(requestDto.getUsername());
        if (user == null) {
            throw new EntityNotFoundException("User not found!");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getUsername(),
                        requestDto.getPassword()
                )
        );
        String token = jwtService.generateToken(user);
        return TokenResponseDto.builder()
                .accessToken(token)
                .build();
    }

}
