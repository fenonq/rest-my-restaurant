package com.spring.myrestaurant.service.impl;

import com.spring.myrestaurant.dto.RoleDto;
import com.spring.myrestaurant.dto.UserDto;
import com.spring.myrestaurant.exception.EntityExistsException;
import com.spring.myrestaurant.exception.EntityNotFoundException;
import com.spring.myrestaurant.model.Dish;
import com.spring.myrestaurant.model.Role;
import com.spring.myrestaurant.model.User;
import com.spring.myrestaurant.repository.DishRepository;
import com.spring.myrestaurant.repository.RoleRepository;
import com.spring.myrestaurant.repository.UserRepository;
import com.spring.myrestaurant.test.util.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private RoleRepository roleRepository;

    @Test
    void findAllTest() {
        List<User> users = List.of(
                TestDataUtil.createUserCustomer(),
                TestDataUtil.createUserManager()
        );
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> returnedUsers = userService.findAll();

        verify(userRepository).findAll();
        assertThat(returnedUsers, hasSize(users.size()));
    }

    @Test
    void findByIdTest() {
        User user = TestDataUtil.createUserCustomer();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto returnedUser = userService.findById(anyLong());

        verify(userRepository).findById(anyLong());
        assertThat(returnedUser, allOf(
                hasProperty("id", equalTo(user.getId())),
                hasProperty("username", equalTo(user.getUsername()))
        ));
    }

    @Test
    void findByIdNotFoundTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findById(anyLong()));
    }

    @Test
    void saveTest() {
        User user = TestDataUtil.createUserCustomer();
        UserDto userDto = TestDataUtil.createUserDtoCustomer();
        Role role = TestDataUtil.createRole();
        when(userRepository.save(any())).thenReturn(user);
        when(roleRepository.findByName(anyString())).thenReturn(role);

        UserDto returnedUser = userService.save(userDto);

        assertThat(returnedUser, allOf(
                hasProperty("id", equalTo(user.getId())),
                hasProperty("username", equalTo(user.getUsername()))
        ));
    }

    @Test
    void saveWithExistingUsernameTest() {
        User user = TestDataUtil.createUserCustomer();
        UserDto userDto = TestDataUtil.createUserDtoCustomer();
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        assertThrows(EntityExistsException.class, () -> userService.save(userDto));
    }

    @Test
    void updateTest() {
        User user = TestDataUtil.createUserCustomer();
        UserDto userDto = TestDataUtil.createUserDtoCustomer();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        UserDto returnedUser = userService.update(user.getId(), userDto);

        assertThat(returnedUser, allOf(
                hasProperty("id", equalTo(user.getId())),
                hasProperty("username", equalTo(user.getUsername()))
        ));
    }

    @Test
    void updateByUsernameTest() {
        User user = TestDataUtil.createUserCustomer();
        UserDto userDto = TestDataUtil.createUserDtoCustomer();
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);

        UserDto returnedUser = userService.update(user.getUsername(), userDto);

        assertThat(returnedUser, allOf(
                hasProperty("id", equalTo(user.getId())),
                hasProperty("username", equalTo(user.getUsername()))
        ));
    }

    @Test
    void updateNotFoundTest() {
        UserDto userDto = TestDataUtil.createUserDtoCustomer();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.update(userDto.getId(), userDto));
    }

    @Test
    void deleteByIdTest() {
        User user = TestDataUtil.createUserCustomer();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.deleteById(anyLong());

        verify(userRepository).delete(user);
    }

    @Test
    void deleteByIdNotFoundTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteById(anyLong()));
    }

    @Test
    void findUserByUsernameTest() {
        User user = TestDataUtil.createUserCustomer();
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        UserDto returnedUser = userService.findUserByUsername(anyString());

        verify(userRepository).findByUsername(anyString());
        assertThat(returnedUser, allOf(
                hasProperty("id", equalTo(user.getId())),
                hasProperty("username", equalTo(user.getUsername()))
        ));
    }

    @Test
    void findUserByUsernameNotFoundTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        UserDto returnedUser = userService.findUserByUsername(anyString());

        verify(userRepository).findByUsername(anyString());
        assertNull(returnedUser);
    }

    @Test
    void addDishToCartTest() {
        User user = TestDataUtil.createUserCustomer();
        Dish dish = TestDataUtil.createDish();
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(dishRepository.findById(anyLong())).thenReturn(Optional.of(dish));

        userService.addDishToCart(user.getUsername(), dish.getId());

        assertThat(user.getCart(), hasSize(1));
        assertTrue(user.getCart().contains(dish));
    }

    @Test
    void addDishToCartDishNotFoundTest() {
        User user = TestDataUtil.createUserCustomer();
        Dish dish = TestDataUtil.createDish();
        when(dishRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.addDishToCart(user.getUsername(), dish.getId()));
    }

    @Test
    void removeDishFromCartTest() {
        User user = TestDataUtil.createUserCustomer();
        Dish dish = TestDataUtil.createDish();
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(dishRepository.findById(anyLong())).thenReturn(Optional.of(dish));

        userService.addDishToCart(user.getUsername(), dish.getId());
        assertTrue(user.getCart().contains(dish));

        userService.removeDishFromCart(user.getUsername(), dish.getId());
        assertTrue(user.getCart().isEmpty());
    }

    @Test
    void removeDishFromCartDishNotFoundTest() {
        User user = TestDataUtil.createUserCustomer();
        Dish dish = TestDataUtil.createDish();
        when(dishRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.removeDishFromCart(user.getUsername(), dish.getId()));
    }

    @Test
    void changeActiveTest() {
        User user = TestDataUtil.createUserCustomer();
        boolean currentVisibility = user.getActive();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto returnedUser;
        for (int i = 0; i < 3; i++) {
            returnedUser = userService.changeActive(user.getId());
            currentVisibility = !currentVisibility;
            assertThat(returnedUser, hasProperty("active", is(currentVisibility)));
        }
    }

    @Test
    void changeActiveNotFoundTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.changeActive(anyLong()));
    }

    @Test
    void changeRoleTest() {
        User user = TestDataUtil.createUserCustomer();
        user.setRoles(new ArrayList<>());
        Role role = TestDataUtil.createRole();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));

        UserDto returnedUser = userService.changeRole(TestDataUtil.ID, anyLong());
        RoleDto roleDto = TestDataUtil.createRoleDto();

        assertThat(returnedUser.getRoles().iterator().next(), is(roleDto));
    }

    @Test
    void findUserDetailsByUsernameTest() {
        User user = TestDataUtil.createUserCustomer();
        user.setRoles(new ArrayList<>());
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        UserDetails returnedUser = userService.loadUserByUsername(anyString());

        verify(userRepository).findByUsername(anyString());
        assertThat(returnedUser, hasProperty("username", equalTo(user.getUsername())));
    }

    @Test
    void findUserDetailsByUsernameNotFoundTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () ->  userService.loadUserByUsername(anyString()));
    }

}
