package com.spring.myrestaurant.service.impl;

import com.spring.myrestaurant.dto.UserDto;
import com.spring.myrestaurant.exception.EntityExistsException;
import com.spring.myrestaurant.exception.EntityNotFoundException;
import com.spring.myrestaurant.mapper.UserMapper;
import com.spring.myrestaurant.model.Dish;
import com.spring.myrestaurant.model.Role;
import com.spring.myrestaurant.model.User;
import com.spring.myrestaurant.repository.DishRepository;
import com.spring.myrestaurant.repository.RoleRepository;
import com.spring.myrestaurant.repository.UserRepository;
import com.spring.myrestaurant.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public List<UserDto> findAll() {
        log.info("find all users");
        return userRepository.findAll()
                .stream()
                .map(UserMapper.INSTANCE::mapUserToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto findById(Long id) {
        log.info("find user with id {}", id);
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return UserMapper.INSTANCE.mapUserToUserDto(user);
    }

    @Override
    @Transactional
    public UserDto save(UserDto userDto) {
        log.info("save user");
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new EntityExistsException("User with this username already exists");
        }
        User user = UserMapper.INSTANCE.mapUserDtoToUser(userDto);
        user.setActive(true);
        user.setRoles(new ArrayList<>(List.of(roleRepository.findByName("ROLE_USER"))));
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user = userRepository.save(user);
        return UserMapper.INSTANCE.mapUserToUserDto(user);
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        log.info("update user with id {}", id);
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        user = userRepository.save(UserMapper.INSTANCE.mapUserWithPresentUserDtoFields(user, userDto));
        return UserMapper.INSTANCE.mapUserToUserDto(user);
    }

    @Override
    @Transactional
    public UserDto update(String username, UserDto userDto) {
        log.info("update user with username {}", username);
        User user = userRepository.findByUsername(username);
        user = userRepository.save(UserMapper.INSTANCE.mapUserWithPresentUserDtoFields(user, userDto));
        return UserMapper.INSTANCE.mapUserToUserDto(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("delete user with id {}", id);
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public UserDto findUserByUsername(String username) {
        log.info("find user with username {}", username);
        User user = userRepository.findByUsername(username);
        return UserMapper.INSTANCE.mapUserToUserDto(user);
    }

    @Override
    @Transactional
    public UserDto addDishToCart(String username, Long dishId) {
        log.info("adding dish {} to user {} cart", dishId, username);
        Dish dish = dishRepository.findById(dishId).orElseThrow(EntityNotFoundException::new);
        User user = userRepository.findByUsername(username);

        if (user != null && dish != null) {
            user.getCart().add(dish);
            user = userRepository.save(user);
        }
        return UserMapper.INSTANCE.mapUserToUserDto(user);
    }

    @Override
    @Transactional
    public UserDto removeDishFromCart(String username, Long dishId) {
        log.info("removing dish {} to user {} cart", dishId, username);
        Dish dish = dishRepository.findById(dishId).orElseThrow(EntityNotFoundException::new);
        User user = userRepository.findByUsername(username);

        if (user != null && dish != null) {
            user.getCart().remove(dish);
            user = userRepository.save(user);
        }
        return UserMapper.INSTANCE.mapUserToUserDto(user);
    }

    @Override
    @Transactional
    public UserDto changeActive(Long userId) {
        log.info("ban/unban user with id {}", userId);
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        userRepository.changUserStatus(userId, !user.getActive());
        user.setActive(!user.getActive());
        return UserMapper.INSTANCE.mapUserToUserDto(user);
    }

    @Override
    @Transactional
    public UserDto changeRole(Long userId, Long roleId) {
        log.info("change user {} role to role {}", userId, roleId);
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        Role role = roleRepository.findById(roleId).orElseThrow(EntityNotFoundException::new);
        user.getRoles().clear();
        user.setRoles(new ArrayList<>(List.of(role)));
        return UserMapper.INSTANCE.mapUserToUserDto(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("User not found in the database");
            throw new EntityNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", username);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

}
