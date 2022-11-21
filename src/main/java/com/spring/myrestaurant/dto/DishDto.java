package com.spring.myrestaurant.dto;

import com.spring.myrestaurant.dto.group.OnCreate;
import com.spring.myrestaurant.dto.group.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishDto {

    @Null(message = "{validation.message.id.null}", groups = OnCreate.class)
    @NotNull(message = "{validation.message.id.notNull}", groups = OnUpdate.class)
    private Long id;

    @NotBlank(message = "{validation.message.name.notBlank}", groups = OnCreate.class)
    private String name;

    @NotBlank(message = "{validation.message.description.notBlank}", groups = OnCreate.class)
    private String description;

    @NotNull(message = "{validation.message.category.notNull}", groups = OnCreate.class)
    private CategoryDto category;

    @Min(value = 1, message = "{validation.message.price.min}")
    @Max(value = 9999, message = "{validation.message.price.max}")
    private int price;

    @Min(value = 1, message = "{validation.message.weight.min}")
    @Max(value = 9999, message = "{validation.message.weight.max}")
    private int weight;

    @Null(message = "{validation.message.visible.null}")
    private Boolean visible;

}
