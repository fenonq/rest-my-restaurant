package com.spring.myrestaurant.dto;

import com.spring.myrestaurant.dto.group.OnCreate;
import com.spring.myrestaurant.dto.group.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusDto {

    @Null(message = "{validation.message.id.null}", groups = OnCreate.class)
    @NotNull(message = "{validation.message.id.notNull}", groups = OnUpdate.class)
    private Long id;

    @NotBlank(message = "{validation.message.name.notBlank}", groups = OnCreate.class)
    private String name;

}
