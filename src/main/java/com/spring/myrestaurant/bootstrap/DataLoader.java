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

            CategoryDto categoryDto1 = categoryService.save(CategoryDto.builder()
                    .name("First dishes")
                    .build());

            DishDto bograch = DishDto.builder()
                    .name("Bograch")
                    .description("Made from meat, sweet peppers, ground paprika," +
                            " tomatoes, potatoes, carrots and spices")
                    .category(categoryDto1)
                    .price(119)
                    .weight(350)
                    .build();
            dishService.save(bograch);

            DishDto borsch = DishDto.builder()
                    .name("Borsch")
                    .description("Made from chopped beets, cabbage with potatoes and various spices")
                    .category(categoryDto1)
                    .price(105)
                    .weight(350)
                    .build();
            dishService.save(borsch);

            DishDto chickenSoup = DishDto.builder()
                    .name("Chicken soup")
                    .description("Made from chicken and vegetables with the addition of pasta")
                    .category(categoryDto1)
                    .price(92)
                    .weight(350)
                    .build();
            dishService.save(chickenSoup);

            DishDto fishSoup = DishDto.builder()
                    .name("Fish soup")
                    .description("Made from fish, vegetables, potatoes and spices")
                    .category(categoryDto1)
                    .price(102)
                    .weight(350)
                    .build();
            dishService.save(fishSoup);

            CategoryDto categoryDto2 = categoryService.save(CategoryDto.builder()
                    .name("Second dishes")
                    .build());

            DishDto cabbageRolls = DishDto.builder()
                    .name("Cabbage rolls")
                    .description("Made from fresh cabbage leaves and meat and rice fillings")
                    .category(categoryDto2)
                    .price(120)
                    .weight(200)
                    .build();
            dishService.save(cabbageRolls);

            DishDto bakedSalmon = DishDto.builder()
                    .name("Baked salmon")
                    .description("Salmon baked with vegetables, potatoes and spices")
                    .category(categoryDto2)
                    .price(250)
                    .weight(200)
                    .build();
            dishService.save(bakedSalmon);

            DishDto potatoPancakes = DishDto.builder()
                    .name("Potato pancakes")
                    .description("Fried potato pancakes in sauce with cherry tomatoes and sour cream")
                    .category(categoryDto2)
                    .price(123)
                    .weight(250)
                    .build();
            dishService.save(potatoPancakes);

            DishDto varenyky = DishDto.builder()
                    .name("Varenyky")
                    .description("Varenyky with potatoes, mushrooms and sour cream")
                    .category(categoryDto2)
                    .price(107)
                    .weight(250)
                    .build();
            dishService.save(varenyky);

            CategoryDto categoryDto3 = categoryService.save(CategoryDto.builder()
                    .name("Salads")
                    .build());

            DishDto greekSalad = DishDto.builder()
                    .name("Greek salad")
                    .description("Salad of cucumbers, tomatoes, olives and feta cheese")
                    .category(categoryDto3)
                    .price(102)
                    .weight(250)
                    .build();
            dishService.save(greekSalad);

            DishDto caesarSalad = DishDto.builder()
                    .name("Caesar salad")
                    .description("Salad of chicken, croutons, parmesan cheese, tomatoes and sauce")
                    .category(categoryDto3)
                    .price(150)
                    .weight(250)
                    .build();
            dishService.save(caesarSalad);

            DishDto cobbSalad = DishDto.builder()
                    .name("Cobb salad")
                    .description("Salad of chicken, bacon, tomatoes, celery, eggs, avocado, cheese and greens")
                    .category(categoryDto3)
                    .price(100)
                    .weight(250)
                    .build();
            dishService.save(cobbSalad);

            DishDto nisuazSalad = DishDto.builder()
                    .name("Nisuaz salad")
                    .description("Salad of fresh vegetables, boiled eggs, anchovies and olive oil")
                    .category(categoryDto3)
                    .price(98)
                    .weight(250)
                    .build();
            dishService.save(nisuazSalad);

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
