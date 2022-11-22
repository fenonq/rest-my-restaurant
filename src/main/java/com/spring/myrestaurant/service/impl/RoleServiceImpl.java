package com.spring.myrestaurant.service.impl;

import com.spring.myrestaurant.dto.RoleDto;
import com.spring.myrestaurant.exception.EntityNotFoundException;
import com.spring.myrestaurant.mapper.RoleMapper;
import com.spring.myrestaurant.model.Role;
import com.spring.myrestaurant.repository.RoleRepository;
import com.spring.myrestaurant.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<RoleDto> findAll() {
        log.info("find all roles");
        return roleRepository.findAll()
                .stream()
                .map(RoleMapper.INSTANCE::mapRoleToRoleDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDto findById(Long id) {
        log.info("find role with id {}", id);
        Role role = roleRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return RoleMapper.INSTANCE.mapRoleToRoleDto(role);
    }

    @Override
    public RoleDto save(RoleDto roleDto) {
        log.info("save role");
        Role role = RoleMapper.INSTANCE.mapRoleDtoToRole(roleDto);
        role = roleRepository.save(role);
        return RoleMapper.INSTANCE.mapRoleToRoleDto(role);
    }

    @Override
    @Transactional
    public RoleDto update(Long id, RoleDto roleDto) {
        log.info("update role with id {}", id);
        roleRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Role role = RoleMapper.INSTANCE.mapRoleDtoToRole(roleDto);
        role = roleRepository.save(role);
        return RoleMapper.INSTANCE.mapRoleToRoleDto(role);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("delete role with id {}", id);
        Role role = roleRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        roleRepository.delete(role);
    }

}
