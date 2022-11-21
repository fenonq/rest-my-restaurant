package com.spring.myrestaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Null
public class ReceiptDto {

    private Long id;
    private UserDto customer;
    private UserDto manager;
    private StatusDto status;
    private Integer totalPrice;
    private LocalDateTime createDate;
    private List<DishDto> dishes = new ArrayList<>();

}
