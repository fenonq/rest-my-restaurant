package com.spring.myrestaurant.controller;

import com.spring.myrestaurant.controller.assembler.ReceiptAssembler;
import com.spring.myrestaurant.controller.model.ReceiptModel;
import com.spring.myrestaurant.dto.ReceiptDto;
import com.spring.myrestaurant.service.ReceiptService;
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

import java.util.Collections;

import static com.spring.myrestaurant.test.util.TestDataUtil.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(ReceiptController.class)
@Import(TestConfig.class)
class ReceiptControllerTest {

    public static final String RECEIPTS_URL = "/api/v1/receipts";

    @MockBean
    private ReceiptService receiptService;

    @MockBean
    private ReceiptAssembler receiptAssembler;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllReceiptsTest() throws Exception {
        ReceiptDto receiptDto = createReceiptDto();
        ReceiptModel receiptModel = new ReceiptModel(receiptDto);

        when(receiptService.findAll()).thenReturn(Collections.singletonList(receiptDto));
        when(receiptAssembler.toModel(receiptDto)).thenReturn(receiptModel);

        mockMvc.perform(get(RECEIPTS_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].totalPrice").value(receiptDto.getTotalPrice()));
        verify(receiptService).findAll();
    }

    @Test
    void getUserReceiptTest() throws Exception {
        ReceiptDto receiptDto = createReceiptDto();
        ReceiptModel receiptModel = new ReceiptModel(receiptDto);

        when(receiptService.findByCustomerUsername(anyString()))
                .thenReturn(Collections.singletonList(receiptDto));
        when(receiptAssembler.toModel(receiptDto)).thenReturn(receiptModel);

        mockMvc.perform(get(RECEIPTS_URL + "/user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].totalPrice").value(receiptDto.getTotalPrice()));
        verify(receiptService).findByCustomerUsername(anyString());
    }

    @Test
    void makeOrderTest() throws Exception {
        ReceiptDto receiptDto = createReceiptDto();
        ReceiptModel receiptModel = new ReceiptModel(receiptDto);

        when(receiptService.makeOrder(anyString())).thenReturn(receiptDto);
        when(receiptAssembler.toModel(receiptDto)).thenReturn(receiptModel);

        mockMvc.perform(post(RECEIPTS_URL)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.totalPrice").value(receiptDto.getTotalPrice()));
        verify(receiptService).makeOrder(anyString());
    }

    @Test
    void nextStatusTest() throws Exception {
        ReceiptDto receiptDto = createReceiptDto();
        ReceiptModel receiptModel = new ReceiptModel(receiptDto);

        when(receiptService.nextStatus(anyLong(), anyString())).thenReturn(receiptDto);
        when(receiptAssembler.toModel(receiptDto)).thenReturn(receiptModel);

        mockMvc.perform(patch(RECEIPTS_URL + "/next-status/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalPrice").value(receiptDto.getTotalPrice()));
        verify(receiptService).nextStatus(anyLong(), anyString());
    }

    @Test
    void cancelOrRenewReceiptTest() throws Exception {
        ReceiptDto receiptDto = createReceiptDto();
        ReceiptModel receiptModel = new ReceiptModel(receiptDto);

        when(receiptService.cancelOrRenewReceipt(anyLong(), anyString())).thenReturn(receiptDto);
        when(receiptAssembler.toModel(receiptDto)).thenReturn(receiptModel);

        mockMvc.perform(patch(RECEIPTS_URL + "/cancel/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalPrice").value(receiptDto.getTotalPrice()));
        verify(receiptService).cancelOrRenewReceipt(anyLong(), anyString());
    }

}
