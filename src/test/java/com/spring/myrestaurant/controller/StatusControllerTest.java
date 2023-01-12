package com.spring.myrestaurant.controller;

import com.spring.myrestaurant.controller.assembler.StatusAssembler;
import com.spring.myrestaurant.controller.model.StatusModel;
import com.spring.myrestaurant.dto.StatusDto;
import com.spring.myrestaurant.jwt.JwtService;
import com.spring.myrestaurant.model.enums.ErrorType;
import com.spring.myrestaurant.service.StatusService;
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
@WebMvcTest(StatusController.class)
@Import(TestConfig.class)
class StatusControllerTest {

    @MockBean
    private JwtService jwtService;

    @MockBean
    private StatusService statusService;

    @MockBean
    private StatusAssembler statusAssembler;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllStatusesTest() throws Exception {
        StatusDto statusDto = createStatusDto();
        StatusModel statusModel = new StatusModel(statusDto);

        when(statusService.findAll()).thenReturn(Collections.singletonList(statusDto));
        when(statusAssembler.toModel(statusDto)).thenReturn(statusModel);

        mockMvc.perform(get(STATUSES_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(statusDto.getName()));
        verify(statusService).findAll();
    }

    @Test
    void getStatusTest() throws Exception {
        StatusDto statusDto = createStatusDto();
        StatusModel statusModel = new StatusModel(statusDto);

        when(statusService.findById(anyLong())).thenReturn(statusDto);
        when(statusAssembler.toModel(statusDto)).thenReturn(statusModel);

        mockMvc.perform(get(STATUSES_URL + "/" + ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(statusDto.getName()));
        verify(statusService).findById(anyLong());
    }

    @Test
    void createStatusTest() throws Exception {
        StatusDto statusDto = createStatusDto();
        statusDto.setId(null);
        StatusModel statusModel = new StatusModel(statusDto);

        when(statusService.save(any(StatusDto.class))).thenReturn(statusDto);
        when(statusAssembler.toModel(statusDto)).thenReturn(statusModel);

        mockMvc.perform(post(STATUSES_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(statusDto.getName()));
        verify(statusService).save(any(StatusDto.class));
    }

    @Test
    void createNotValidStatusTest() throws Exception {
        StatusDto statusDto = StatusDto.builder().id(ID).build();
        StatusModel statusModel = new StatusModel(statusDto);

        when(statusService.save(any(StatusDto.class))).thenReturn(statusDto);
        when(statusAssembler.toModel(statusDto)).thenReturn(statusModel);

        mockMvc.perform(post(STATUSES_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$[0].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name()),
                        jsonPath("$[1].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name())
                );
    }

    @Test
    void updateStatusTest() throws Exception {
        StatusDto statusDto = createStatusDto();
        StatusModel statusModel = new StatusModel(statusDto);

        when(statusService.update(anyLong(), any(StatusDto.class))).thenReturn(statusDto);
        when(statusAssembler.toModel(statusDto)).thenReturn(statusModel);

        mockMvc.perform(put(STATUSES_URL + "/" + ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(statusDto.getName()));
        verify(statusService).update(anyLong(), any(StatusDto.class));
    }

    @Test
    void updateNotValidStatusTest() throws Exception {
        StatusDto statusDto = StatusDto.builder().build();
        StatusModel statusModel = new StatusModel(statusDto);

        when(statusService.update(anyLong(), any(StatusDto.class))).thenReturn(statusDto);
        when(statusAssembler.toModel(statusDto)).thenReturn(statusModel);

        mockMvc.perform(put(STATUSES_URL + "/" + ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name()));
    }

}
