package com.spring.myrestaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.myrestaurant.dto.AuthenticationRequestDto;
import com.spring.myrestaurant.dto.UserDto;
import com.spring.myrestaurant.jwt.JwtService;
import com.spring.myrestaurant.model.enums.ErrorType;
import com.spring.myrestaurant.service.AuthenticationService;
import com.spring.myrestaurant.service.UserService;
import com.spring.myrestaurant.test.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.spring.myrestaurant.test.util.TestDataUtil.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthenticationController.class)
@Import(TestConfig.class)
class AuthenticationControllerTest {

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void signup() throws Exception {
        UserDto userDto = createUserDtoCustomer();
        userDto.setId(null);
        userDto.setActive(null);
        userDto.setCart(null);

        when(authenticationService.signup(any(UserDto.class))).thenReturn(createTokenResponseDto());

        mockMvc.perform(post(AUTH_URL + "/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN));
        verify(authenticationService).signup(any(UserDto.class));
    }

    @Test
    void signupNotValidUserTest() throws Exception {
        UserDto userDto = UserDto.builder().id(ID).build();

        when(authenticationService.signup(any(UserDto.class))).thenReturn(createTokenResponseDto());

        mockMvc.perform(post(AUTH_URL + "/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$[0].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name()),
                        jsonPath("$[1].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name()),
                        jsonPath("$[2].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name()),
                        jsonPath("$[3].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name()),
                        jsonPath("$[4].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name())
                );
    }

    @Test
    void login() throws Exception {
        AuthenticationRequestDto authenticationRequest = createAuthenticationRequestDto();

        when(authenticationService.login(any(AuthenticationRequestDto.class)))
                .thenReturn(createTokenResponseDto());

        mockMvc.perform(post(AUTH_URL + "/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN));
        verify(authenticationService).login(any(AuthenticationRequestDto.class));
    }

}
