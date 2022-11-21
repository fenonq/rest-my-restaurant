package com.spring.myrestaurant.service.impl;

import com.spring.myrestaurant.dto.RoleDto;
import com.spring.myrestaurant.exception.EntityNotFoundException;
import com.spring.myrestaurant.model.Role;
import com.spring.myrestaurant.repository.RoleRepository;
import com.spring.myrestaurant.test.util.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    void findAllTest() {
        List<Role> roles = List.of(
                TestDataUtil.createRole(),
                TestDataUtil.createRole()
        );
        when(roleRepository.findAll()).thenReturn(roles);

        List<RoleDto> returnedRoles = roleService.findAll();

        verify(roleRepository).findAll();
        assertThat(returnedRoles, hasSize(roles.size()));
    }

    @Test
    void findByIdTest() {
        Role role = TestDataUtil.createRole();
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));

        RoleDto returnedRole = roleService.findById(anyLong());

        verify(roleRepository).findById(anyLong());
        assertThat(returnedRole, allOf(
                hasProperty("id", equalTo(role.getId())),
                hasProperty("name", equalTo(role.getName()))
        ));
    }

    @Test
    void findByIdNotFoundTest() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> roleService.findById(anyLong()));
    }

    @Test
    void saveTest() {
        Role role = TestDataUtil.createRole();
        RoleDto roleDto = TestDataUtil.createRoleDto();
        when(roleRepository.save(any())).thenReturn(role);

        RoleDto returnedRole = roleService.save(roleDto);

        assertThat(returnedRole, allOf(
                hasProperty("id", equalTo(roleDto.getId())),
                hasProperty("name", equalTo(roleDto.getName()))
        ));
    }

    @Test
    void updateTest() {
        Role role = TestDataUtil.createRole();
        RoleDto roleDto = TestDataUtil.createRoleDto();
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));
        when(roleRepository.save(any())).thenReturn(role);

        RoleDto returnedRole = roleService.update(role.getId(), roleDto);

        assertThat(returnedRole, allOf(
                hasProperty("id", equalTo(roleDto.getId())),
                hasProperty("name", equalTo(roleDto.getName()))
        ));
    }

    @Test
    void updateNotFoundTest() {
        RoleDto roleDto = TestDataUtil.createRoleDto();

        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> roleService.update(roleDto.getId(), roleDto));
    }

    @Test
    void deleteByIdTest() {
        Role role = TestDataUtil.createRole();
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));

        roleService.deleteById(anyLong());

        verify(roleRepository).delete(role);
    }

    @Test
    void deleteByIdNotFoundTest() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> roleService.deleteById(anyLong()));
    }

}
