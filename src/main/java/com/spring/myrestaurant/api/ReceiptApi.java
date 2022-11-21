package com.spring.myrestaurant.api;

import com.spring.myrestaurant.controller.model.ReceiptModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "Receipt management api")
@RequestMapping("/api/v1/receipts")
public interface ReceiptApi {

    @ApiOperation("Get all receipts")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<ReceiptModel> getAllReceipts();

    @ApiOperation("Get all user receipts")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user")
    List<ReceiptModel> getUserReceipts(@ApiIgnore Authentication authentication);

    @ApiOperation("Create receipt")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    ReceiptModel makeOrder(@ApiIgnore Authentication authentication);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "receiptId", paramType = "path", required = true, value = "Receipt id")
    })
    @ApiOperation("Change receipt status")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/next-status/{receiptId}")
    ReceiptModel nextStatus(@PathVariable Long receiptId, @ApiIgnore Authentication authentication);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "receiptId", paramType = "path", required = true, value = "Receipt id")
    })
    @ApiOperation("Cancel receipt status")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/cancel/{receiptId}")
    ReceiptModel cancelOrRenewReceipt(@PathVariable Long receiptId, @ApiIgnore Authentication authentication);

}
