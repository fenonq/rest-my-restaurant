package com.spring.myrestaurant.controller;

import com.spring.myrestaurant.controller.assembler.UserAssembler;
import com.spring.myrestaurant.controller.model.UserModel;
import com.spring.myrestaurant.dto.UserDto;
import com.spring.myrestaurant.exception.EntityNotFoundException;
import com.spring.myrestaurant.model.enums.ErrorType;
import com.spring.myrestaurant.service.UserService;
import com.spring.myrestaurant.test.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Collections;

import static com.spring.myrestaurant.test.util.TestDataUtil.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@Import(TestConfig.class)
class UserControllerTest {

    public static final String USERS_URL = "/api/v1/users";

    @MockBean
    private UserService userService;

    @MockBean
    private UserAssembler userAssembler;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllUsersTest() throws Exception {
        UserDto userDto = createUserDtoCustomer();
        UserModel userModel = new UserModel(userDto);

        when(userService.findAll()).thenReturn(Collections.singletonList(userDto));
        when(userAssembler.toModel(userDto)).thenReturn(userModel);

        mockMvc.perform(get(USERS_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username").value(userDto.getUsername()));
        verify(userService).findAll();
    }

    @Test
    void getUserTest() throws Exception {
        UserDto userDto = createUserDtoCustomer();
        UserModel userModel = new UserModel(userDto);

        when(userService.findById(anyLong())).thenReturn(userDto);
        when(userAssembler.toModel(userDto)).thenReturn(userModel);

        mockMvc.perform(get(USERS_URL + "/" + ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.username").value(userDto.getUsername()));
        verify(userService).findById(anyLong());
    }

    @Test
    void getUserNotFoundTest() throws Exception {
        when(userService.findById(anyLong())).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get(USERS_URL + "/" + ID))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name()));
        verify(userService).findById(anyLong());
    }

    @Test
    void fatalErrorTest() throws Exception {
        when(userService.findById(anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(get(USERS_URL + "/" + ID))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorType").value(ErrorType.FATAL_ERROR_TYPE.name()));
    }

    @Test
    void createUserTest() throws Exception {
        UserDto userDto = createUserDtoCustomer();
        userDto.setId(null);
        userDto.setActive(null);
        userDto.setCart(null);
        UserModel userModel = new UserModel(userDto);

        when(userService.save(any(UserDto.class))).thenReturn(userDto);
        when(userAssembler.toModel(userDto)).thenReturn(userModel);

        mockMvc.perform(post(USERS_URL + "/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(userDto.getUsername()));
        verify(userService).save(any(UserDto.class));
    }

    @Test
    void createNotValidUserTest() throws Exception {
        UserDto userDto = UserDto.builder().id(ID).build();
        UserModel userModel = new UserModel(userDto);

        when(userService.save(any(UserDto.class))).thenReturn(userDto);
        when(userAssembler.toModel(userDto)).thenReturn(userModel);

        mockMvc.perform(post(USERS_URL + "/signup")
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
    void updateUserTest() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();
        UserModel userModel = new UserModel(userDto);

        when(userService.update(anyString(), any(UserDto.class))).thenReturn(userDto);
        when(userAssembler.toModel(userDto)).thenReturn(userModel);

        mockMvc.perform(put(USERS_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(userDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDto.getLastName()));
        verify(userService).update(anyString(), any(UserDto.class));
    }

    @Test
    void updateNotValidUserTest() throws Exception {
        UserDto userDto = createUserDtoCustomer();
        userDto.setId(null);
        userDto.setFirstName(null);
        userDto.setLastName(null);
        UserModel userModel = new UserModel(userDto);

        when(userService.update(anyString(), any(UserDto.class))).thenReturn(userDto);
        when(userAssembler.toModel(userDto)).thenReturn(userModel);

        mockMvc.perform(put(USERS_URL)
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
    void addDishToCartTest() throws Exception {
        UserDto userDto = createUserDtoCustomer();
        UserModel userModel = new UserModel(userDto);

        when(userService.addDishToCart(anyString(), anyLong())).thenReturn(userDto);
        when(userAssembler.toModel(userDto)).thenReturn(userModel);

        mockMvc.perform(patch(USERS_URL + "/cart/add/" + ID)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(userDto.getUsername()));
        verify(userService).addDishToCart(anyString(), anyLong());
    }

    @Test
    void removeDishFromCartTest() throws Exception {
        UserDto userDto = createUserDtoCustomer();
        UserModel userModel = new UserModel(userDto);

        when(userService.removeDishFromCart(anyString(), anyLong())).thenReturn(userDto);
        when(userAssembler.toModel(userDto)).thenReturn(userModel);

        mockMvc.perform(patch(USERS_URL + "/cart/remove/" + ID)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(userDto.getUsername()));
        verify(userService).removeDishFromCart(anyString(), anyLong());
    }

    @Test
    void banUserTest() throws Exception {
        UserDto userDto = createUserDtoCustomer();
        UserModel userModel = new UserModel(userDto);

        when(userService.changeActive(anyLong())).thenReturn(userDto);
        when(userAssembler.toModel(userDto)).thenReturn(userModel);

        mockMvc.perform(patch(USERS_URL + "/ban/" + ID)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(userDto.getUsername()));
        verify(userService).changeActive(anyLong());
    }

    @Test
    void changeRoleTest() throws Exception {
        UserDto userDto = createUserDtoCustomer();
        UserModel userModel = new UserModel(userDto);

        when(userService.changeRole(anyLong(), anyLong())).thenReturn(userDto);
        when(userAssembler.toModel(userDto)).thenReturn(userModel);

        mockMvc.perform(patch(USERS_URL + "/1/change-role/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(userDto.getUsername()));
        verify(userService).changeRole(anyLong(), anyLong());
    }

}
