package com.spring.myrestaurant.bootstrap;

import com.spring.myrestaurant.dto.*;
import com.spring.myrestaurant.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;
    private final DishService dishService;
    private final StatusService statusService;
    private final CategoryService categoryService;

    @Override
    public void run(String... args) {
        if (userService.findAll().isEmpty()) {
            RoleDto role_user = roleService.save(RoleDto.builder()
                    .name("ROLE_USER")
                    .build());

            RoleDto role_manager = roleService.save(RoleDto.builder()
                    .name("ROLE_MANAGER")
                    .build());

            RoleDto role_admin = roleService.save(RoleDto.builder()
                    .name("ROLE_ADMIN")
                    .build());

            UserDto userDto = userService.save(UserDto.builder()
                    .firstName("user")
                    .lastName("user")
                    .username("user")
                    .password("user")
                    .build());

            UserDto userDto1 = userService.save(UserDto.builder()
                    .firstName("manager")
                    .lastName("manager")
                    .username("manager")
                    .password("manager")
                    .build());

            UserDto userDto2 = userService.save(UserDto.builder()
                    .firstName("admin")
                    .lastName("admin")
                    .username("admin")
                    .password("admin")
                    .build());

            userService.changeRole(userDto.getId(), role_user.getId());
            userService.changeRole(userDto1.getId(), role_manager.getId());
            userService.changeRole(userDto2.getId(), role_admin.getId());

            CategoryDto categoryDto1 = CategoryDto.builder()
                    .name("First dishes")
                    .build();

            DishDto dishDto1 = DishDto.builder()
                    .name("Borsch")
                    .description("Made from chopped beets, cabbage with potatoes and various spices")
                    .category(categoryService.save(categoryDto1))
                    .price(105)
                    .weight(350)
                    .build();
            dishService.save(dishDto1);

            CategoryDto categoryDto2 = CategoryDto.builder()
                    .name("Second dishes")
                    .build();

            DishDto dishDto2 = DishDto.builder()
                    .name("Varenyky")
                    .description("Varenyky with potatoes, mushrooms and sour cream")
                    .category(categoryService.save(categoryDto2))
                    .price(107)
                    .weight(250)
                    .build();
            dishService.save(dishDto2);

            DishDto dishDto3 = DishDto.builder()
                    .name("Potato pancakes")
                    .description("Fried potato pancakes in sauce with cherry tomatoes and sour cream")
                    .category(categoryService.save(categoryDto2))
                    .price(123)
                    .weight(250)
                    .build();
            dishService.save(dishDto3);

            CategoryDto categoryDto3 = CategoryDto.builder()
                    .name("Second dishes")
                    .build();

            DishDto dishDto4 = DishDto.builder()
                    .name("Caesar salad")
                    .description("Salad of chicken, croutons, parmesan cheese, tomatoes and sauce")
                    .category(categoryService.save(categoryDto3))
                    .price(150)
                    .weight(250)
                    .build();
            dishService.save(dishDto4);

            statusService.save(StatusDto.builder()
                    .name("New")
                    .build());
            statusService.save(StatusDto.builder()
                    .name("Accepted")
                    .build());
            statusService.save(StatusDto.builder()
                    .name("Cooking")
                    .build());
            statusService.save(StatusDto.builder()
                    .name("Delivering")
                    .build());
            statusService.save(StatusDto.builder()
                    .name("Done")
                    .build());
            statusService.save(StatusDto.builder()
                    .name("Canceled")
                    .build());
        }
    }

}
