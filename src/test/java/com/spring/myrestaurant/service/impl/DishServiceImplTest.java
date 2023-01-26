package com.spring.myrestaurant.service.impl;

import com.spring.myrestaurant.dto.DishDto;
import com.spring.myrestaurant.exception.EntityNotFoundException;
import com.spring.myrestaurant.model.Dish;
import com.spring.myrestaurant.repository.DishRepository;
import com.spring.myrestaurant.test.util.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishServiceImplTest {

    @InjectMocks
    private DishServiceImpl dishService;

    @Mock
    private DishRepository dishRepository;

    @Test
    void findAllTest() {
        List<Dish> dishes = List.of(
                TestDataUtil.createDish(),
                TestDataUtil.createDish()
        );
        when(dishRepository.findAll()).thenReturn(dishes);

        List<DishDto> returnedDishes = dishService.findAll();

        verify(dishRepository).findAll();
        assertThat(returnedDishes, hasSize(dishes.size()));
    }

    @Test
    void findByIdTest() {
        Dish dish = TestDataUtil.createDish();
        when(dishRepository.findById(anyLong())).thenReturn(Optional.of(dish));

        DishDto returnedDish = dishService.findById(anyLong());

        verify(dishRepository).findById(anyLong());
        assertThat(returnedDish, allOf(
                hasProperty("id", equalTo(dish.getId())),
                hasProperty("name", equalTo(dish.getName()))
        ));
    }

    @Test
    void findByIdNotFoundTest() {
        when(dishRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> dishService.findById(anyLong()));
    }

    @Test
    void saveTest() {
        Dish dish = TestDataUtil.createDish();
        DishDto dishDto = TestDataUtil.createDishDto();
        when(dishRepository.save(any())).thenReturn(dish);

        DishDto returnedDish = dishService.save(dishDto);

        assertThat(returnedDish, allOf(
                hasProperty("id", equalTo(dishDto.getId())),
                hasProperty("name", equalTo(dishDto.getName()))
        ));
    }

    @Test
    void updateTest() {
        Dish dish = TestDataUtil.createDish();
        DishDto dishDto = TestDataUtil.createDishDto();
        when(dishRepository.findById(anyLong())).thenReturn(Optional.of(dish));
        when(dishRepository.save(any())).thenReturn(dish);

        DishDto returnedDish = dishService.update(dish.getId(), dishDto);

        assertThat(returnedDish, allOf(
                hasProperty("id", equalTo(dishDto.getId())),
                hasProperty("name", equalTo(dishDto.getName()))
        ));
    }

    @Test
    void updateNotFoundTest() {
        DishDto dishDto = TestDataUtil.createDishDto();

        when(dishRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> dishService.update(dishDto.getId(), dishDto));
    }

    @Test
    void deleteByIdTest() {
        Dish dish = TestDataUtil.createDish();
        when(dishRepository.findById(anyLong())).thenReturn(Optional.of(dish));

        dishService.deleteById(anyLong());

        verify(dishRepository).delete(dish);
    }

    @Test
    void deleteByIdNotFoundTest() {
        when(dishRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> dishService.deleteById(anyLong()));
    }

    @Test
    void findAllPageableTest() {
        List<Dish> dishes = List.of(
                TestDataUtil.createDish(),
                TestDataUtil.createDish(),
                TestDataUtil.createDish(),
                TestDataUtil.createDish(),
                TestDataUtil.createDish()
        );
        int size = 2;
        Pageable pageable = PageRequest.of(0, size);

        when(dishRepository.findAllByVisible(pageable, Boolean.TRUE))
                .thenReturn(new PageImpl<>(dishes, pageable, dishes.size()));

        Page<DishDto> returnedDishes = dishService.findAllByVisible(pageable);

        verify(dishRepository).findAllByVisible(pageable, Boolean.TRUE);
        assertThat(returnedDishes.getPageable(), is(pageable));
        assertThat(returnedDishes.getSize(), is(size));
    }

    @Test
    void changeVisibilityTest() {
        Dish dish = TestDataUtil.createDish();
        boolean currentVisibility = dish.isVisible();
        when(dishRepository.findById(anyLong())).thenReturn(Optional.of(dish));

        DishDto returnedDish;
        for (int i = 0; i < 3; i++) {
            returnedDish = dishService.changeVisibility(dish.getId());
            currentVisibility = !currentVisibility;
            assertThat(returnedDish, hasProperty("visible", is(currentVisibility)));
        }
    }

    @Test
    void changeVisibilityNotFoundTest() {
        when(dishRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> dishService.changeVisibility(anyLong()));
    }

}
