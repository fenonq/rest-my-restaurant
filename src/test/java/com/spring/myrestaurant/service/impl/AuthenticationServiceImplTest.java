package com.spring.myrestaurant.service.impl;

import com.spring.myrestaurant.dto.AuthenticationRequestDto;
import com.spring.myrestaurant.dto.TokenResponseDto;
import com.spring.myrestaurant.dto.UserDto;
import com.spring.myrestaurant.exception.EntityExistsException;
import com.spring.myrestaurant.exception.EntityNotFoundException;
import com.spring.myrestaurant.jwt.JwtService;
import com.spring.myrestaurant.model.Role;
import com.spring.myrestaurant.model.User;
import com.spring.myrestaurant.repository.RoleRepository;
import com.spring.myrestaurant.repository.UserRepository;
import com.spring.myrestaurant.test.util.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void signupTest() {
        User user = TestDataUtil.createUserCustomer();
        UserDto userDto = TestDataUtil.createUserDtoCustomer();
        Role role = TestDataUtil.createRole();

        when(userRepository.save(any())).thenReturn(user);
        when(roleRepository.findByName(anyString())).thenReturn(role);
        when(jwtService.generateToken(any())).thenReturn(TestDataUtil.ACCESS_TOKEN);

        TokenResponseDto returnedToken = authenticationService.signup(userDto);

        assertThat(returnedToken.getAccessToken(), not(blankOrNullString()));
        assertThat(returnedToken.getAccessToken(), is(TestDataUtil.ACCESS_TOKEN));
    }

    @Test
    void signupUserExistTest() {
        User user = TestDataUtil.createUserCustomer();
        UserDto userDto = TestDataUtil.createUserDtoCustomer();
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        assertThrows(EntityExistsException.class, () -> authenticationService.signup(userDto));
    }

    @Test
    void loginTest() {
        AuthenticationRequestDto authentication = TestDataUtil.createAuthenticationRequestDto();
        User user = TestDataUtil.createUserCustomer();

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(jwtService.generateToken(any())).thenReturn(TestDataUtil.ACCESS_TOKEN);

        TokenResponseDto returnedToken = authenticationService.login(authentication);

        assertThat(returnedToken.getAccessToken(), not(blankOrNullString()));
        assertThat(returnedToken.getAccessToken(), is(TestDataUtil.ACCESS_TOKEN));
    }

    @Test
    void loginUserNotFoundTest() {
        AuthenticationRequestDto authentication = TestDataUtil.createAuthenticationRequestDto();

        assertThrows(EntityNotFoundException.class, () -> authenticationService.login(authentication));
    }

}
