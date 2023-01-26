package com.spring.myrestaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.myrestaurant.controller.assembler.DishAssembler;
import com.spring.myrestaurant.controller.model.DishModel;
import com.spring.myrestaurant.dto.DishDto;
import com.spring.myrestaurant.jwt.JwtService;
import com.spring.myrestaurant.model.enums.ErrorType;
import com.spring.myrestaurant.service.DishService;
import com.spring.myrestaurant.test.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.spring.myrestaurant.test.util.TestDataUtil.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(DishController.class)
@Import(TestConfig.class)
class DishControllerTest {

    @MockBean
    private JwtService jwtService;

    @MockBean
    private DishService dishService;

    @MockBean
    private DishAssembler dishAssembler;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllPageableDishesTest() throws Exception {
        int page = 0;
        int size = 2;
        DishDto dishDto = createDishDto();
        DishModel dishModel = new DishModel(dishDto);
        List<DishDto> dishDtoList = Arrays.asList(dishDto, dishDto);
        Pageable pageable = PageRequest.of(page, size);

        Page<DishDto> dishDtoPage = new PageImpl<>(dishDtoList, pageable, dishDtoList.size());

        when(dishService.findAllByVisible(any(Pageable.class))).thenReturn(dishDtoPage);
        when(dishAssembler.toModel(dishDto)).thenReturn(dishModel);

        mockMvc.perform(get(DISHES_URL + "/pageable")
                        .queryParam("page", String.valueOf(page))
                        .queryParam("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.pageable.pageNumber").value(page))
                .andExpect(jsonPath("$.pageable.pageSize").value(size))
                .andExpect(jsonPath("$.content[0].name").value(dishDto.getName()));
        verify(dishService).findAllByVisible(any(Pageable.class));
    }

    @Test
    void getAllDishesTest() throws Exception {
        DishDto dishDto = createDishDto();
        DishModel dishModel = new DishModel(dishDto);

        when(dishService.findAll()).thenReturn(Collections.singletonList(dishDto));
        when(dishAssembler.toModel(dishDto)).thenReturn(dishModel);

        mockMvc.perform(get(DISHES_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(dishDto.getName()));
        verify(dishService).findAll();
    }

    @Test
    void getDishTest() throws Exception {
        DishDto dishDto = createDishDto();
        DishModel dishModel = new DishModel(dishDto);

        when(dishService.findById(anyLong())).thenReturn(dishDto);
        when(dishAssembler.toModel(dishDto)).thenReturn(dishModel);

        mockMvc.perform(get(DISHES_URL + "/" + ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(dishDto.getName()));
        verify(dishService).findById(anyLong());
    }

    @Test
    void createDishTest() throws Exception {
        DishDto dishDto = createDishDto();
        dishDto.setId(null);
        dishDto.setVisible(null);
        DishModel dishModel = new DishModel(dishDto);

        when(dishService.save(any(DishDto.class))).thenReturn(dishDto);
        when(dishAssembler.toModel(dishDto)).thenReturn(dishModel);

        mockMvc.perform(post(DISHES_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(dishDto.getName()));
        verify(dishService).save(any(DishDto.class));
    }

    @Test
    void createNotValidDishTest() throws Exception {
        DishDto dishDto = DishDto.builder().id(ID).build();
        DishModel dishModel = new DishModel(dishDto);

        when(dishService.save(any(DishDto.class))).thenReturn(dishDto);
        when(dishAssembler.toModel(dishDto)).thenReturn(dishModel);

        mockMvc.perform(post(DISHES_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$[0].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name()),
                        jsonPath("$[1].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name()),
                        jsonPath("$[2].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name()),
                        jsonPath("$[3].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name()),
                        jsonPath("$[4].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name()),
                        jsonPath("$[5].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name())
                );
    }

    @Test
    void updateDishTest() throws Exception {
        DishDto dishDto = createDishDto();
        dishDto.setVisible(null);
        DishModel dishModel = new DishModel(dishDto);

        when(dishService.update(anyLong(), any(DishDto.class))).thenReturn(dishDto);
        when(dishAssembler.toModel(dishDto)).thenReturn(dishModel);

        mockMvc.perform(put(DISHES_URL + "/" + ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(dishDto.getName()));
        verify(dishService).update(anyLong(), any(DishDto.class));
    }

    @Test
    void updateNotValidDishTest() throws Exception {
        DishDto dishDto = DishDto.builder().build();
        DishModel dishModel = new DishModel(dishDto);

        when(dishService.update(anyLong(), any(DishDto.class))).thenReturn(dishDto);
        when(dishAssembler.toModel(dishDto)).thenReturn(dishModel);

        mockMvc.perform(put(DISHES_URL + "/" + ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$[0].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name()),
                        jsonPath("$[1].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name()),
                        jsonPath("$[2].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name())
                );
    }

    @Test
    void changeVisibilityTest() throws Exception {
        DishDto dishDto = createDishDto();
        DishModel dishModel = new DishModel(dishDto);

        when(dishService.changeVisibility(anyLong())).thenReturn(dishDto);
        when(dishAssembler.toModel(dishDto)).thenReturn(dishModel);

        mockMvc.perform(patch(DISHES_URL + "/visibility/" + ID)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(dishDto.getName()));
        verify(dishService).changeVisibility(anyLong());
    }

}
