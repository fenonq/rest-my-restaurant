package com.spring.myrestaurant.api;

import com.spring.myrestaurant.controller.model.CategoryModel;
import com.spring.myrestaurant.dto.CategoryDto;
import com.spring.myrestaurant.dto.group.OnCreate;
import com.spring.myrestaurant.dto.group.OnUpdate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Category management api")
@RequestMapping("/api/v1/categories")
public interface CategoryApi {

    @ApiOperation("Get all categories")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<CategoryModel> getAllCategories();

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", required = true, value = "Category id")
    })
    @ApiOperation("Get category by id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    CategoryModel getCategory(@PathVariable Long id);

    @ApiOperation("Create category")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    CategoryModel createCategory(@RequestBody @Validated(OnCreate.class) CategoryDto categoryDto);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", required = true, value = "Category id")
    })
    @ApiOperation("Update category")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}")
    CategoryModel updateCategory(@PathVariable Long id,
                                 @RequestBody @Validated(OnUpdate.class) CategoryDto categoryDto);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", required = true, value = "Category id")
    })
    @ApiOperation("Change category visibility")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/visibility/{id}")
    CategoryModel changeVisibility(@PathVariable Long id);

}
