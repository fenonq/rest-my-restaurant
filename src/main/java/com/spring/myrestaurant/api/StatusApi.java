package com.spring.myrestaurant.api;

import com.spring.myrestaurant.controller.model.StatusModel;
import com.spring.myrestaurant.dto.StatusDto;
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

@Api(tags = "Status management api")
@RequestMapping("/api/v1/statuses")
public interface StatusApi {

    @ApiOperation("Get all statuses")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<StatusModel> getAllStatuses();

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", required = true, value = "Status id")
    })
    @ApiOperation("Get status by id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    StatusModel getStatus(@PathVariable Long id);

    @ApiOperation("Create status")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    StatusModel createStatus(@RequestBody @Validated(OnCreate.class) StatusDto statusDto);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", required = true, value = "Status id")
    })
    @ApiOperation("Update status")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}")
    StatusModel updateStatus(@PathVariable Long id,
                             @RequestBody @Validated(OnUpdate.class) StatusDto statusDto);

}
