package com.spring.myrestaurant.api;

import com.spring.myrestaurant.controller.model.DishModel;
import com.spring.myrestaurant.dto.DishDto;
import com.spring.myrestaurant.dto.group.OnCreate;
import com.spring.myrestaurant.dto.group.OnUpdate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Dish management api")
@RequestMapping("/api/v1/dishes")
public interface DishApi {

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.", defaultValue = "5"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    @ApiOperation("Get all pageable dishes")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    Page<DishModel> getAllDishes(@ApiIgnore("Ignored because swagger ui shows the wrong params")
                                         Pageable pageable);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", required = true, value = "Dish id")
    })
    @ApiOperation("Get dish by id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    DishModel getDish(@PathVariable Long id);

    @ApiOperation("Create dish")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    DishModel createDish(@RequestBody @Validated(OnCreate.class) DishDto dishDto);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", required = true, value = "Dish id")
    })
    @ApiOperation("Update dish")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}")
    DishModel updateDish(@PathVariable Long id, @RequestBody @Validated(OnUpdate.class) DishDto dishDto);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", required = true, value = "dish id")
    })
    @ApiOperation("Change dish visibility")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/visibility/{id}")
    DishModel changeVisibility(@PathVariable Long id);

}
