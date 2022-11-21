package com.spring.myrestaurant.test.util;

import com.spring.myrestaurant.dto.*;
import com.spring.myrestaurant.model.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestDataUtil {

    public static final Long ID = 1L;
    public static final String NAME = "Name";
    public static final String DESCRIPTION = "Description";
    public static final String FIRST_NAME = "FirstName";
    public static final String LAST_NAME = "LastName";
    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";
    public static final String ROLE_USER = "ROLE_USER";
    public static final int PRICE = 100;
    public static final int WEIGHT = 100;

    public static Category createCategory() {
        return Category.builder()
                .id(ID)
                .name(NAME)
                .visible(true)
                .dishes(new HashSet<>())
                .build();
    }

    public static CategoryDto createCategoryDto() {
        return CategoryDto.builder()
                .id(ID)
                .name(NAME)
                .visible(true)
                .build();
    }

    public static Status createStatus() {
        return Status.builder()
                .id(ID)
                .name(NAME)
                .build();
    }

    public static Status createStatus(Long id, String name) {
        return Status.builder()
                .id(id)
                .name(name)
                .build();
    }

    public static StatusDto createStatusDto() {
        return StatusDto.builder()
                .id(ID)
                .name(NAME)
                .build();
    }

    public static User createUserCustomer() {
        return User.builder()
                .id(ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .username(USERNAME)
                .password(PASSWORD)
//                .roles(new ArrayList<>())
                .active(true)
                .cart(new ArrayList<>())
                .build();
    }

    public static User createUserManager() {
        return User.builder()
                .id(ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .username(USERNAME)
                .password(PASSWORD)
//                .role(Roles.MANAGER)
                .active(true)
                .cart(new ArrayList<>())
                .build();
    }

    public static UserDto createUserDtoCustomer() {
        return UserDto.builder()
                .id(ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .username(USERNAME)
                .password(PASSWORD)
//                .role(Roles.CUSTOMER)
                .active(true)
                .cart(new ArrayList<>())
                .build();
    }

    public static Dish createDish() {
        return Dish.builder()
                .id(ID)
                .name(NAME)
                .description(DESCRIPTION)
                .category(createCategory())
                .price(PRICE)
                .weight(WEIGHT)
                .visible(true)
                .build();
    }

    public static DishDto createDishDto() {
        return DishDto.builder()
                .id(ID)
                .name(NAME)
                .description(DESCRIPTION)
                .category(createCategoryDto())
                .price(PRICE)
                .weight(WEIGHT)
                .visible(true)
                .build();
    }

    public static Receipt createReceipt() {
        return Receipt.builder()
                .id(ID)
                .dishes(List.of(createDish()))
                .status(createStatus())
                .createDate(LocalDateTime.now())
                .customer(createUserCustomer())
                .totalPrice(PRICE)
                .build();
    }

    public static ReceiptDto createReceiptDto() {
        return ReceiptDto.builder()
                .id(ID)
                .dishes(List.of(createDishDto()))
                .status(createStatusDto())
                .createDate(LocalDateTime.now())
                .customer(createUserDtoCustomer())
                .totalPrice(PRICE)
                .build();
    }

    public static Role createRole() {
        return Role.builder()
                .id(ID)
                .name(NAME)
                .build();
    }

    public static RoleDto createRoleDto() {
        return RoleDto.builder()
                .id(ID)
                .name(NAME)
                .build();
    }

}
