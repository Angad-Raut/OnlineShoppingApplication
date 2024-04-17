package com.projectx.microservice.adminservice.controller;

import com.projectx.microservice.adminservice.payloads.DiscountDto;
import com.projectx.microservice.adminservice.payloads.DiscountValueDto;
import com.projectx.microservice.adminservice.payloads.ViewDiscountDto;
import com.projectx.microservice.adminservice.service.DiscountService;
import com.projectx.microservice.exceptions.AlreadyExistException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.payloads.EntityIdDto;
import com.projectx.microservice.payloads.EntityTypeDto;
import com.projectx.microservice.payloads.ResponseDto;
import com.projectx.microservice.utils.ErrorHandlerComponent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discount")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @Autowired
    private ErrorHandlerComponent errorHandler;

    @PostMapping(value = "/addDiscount")
    public ResponseEntity<ResponseDto<Boolean>> addDiscount(
            @Valid @RequestBody DiscountDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try{
            Boolean data = discountService.addUpdateDiscount(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null), HttpStatus.CREATED);
        } catch (ResourceNotFoundException | AlreadyExistException e) {
            return errorHandler.handleError(e);
        }
    }
    @PostMapping(value = "/getDiscount")
    public ResponseEntity<ResponseDto<DiscountDto>> getDiscount(
            @Valid @RequestBody EntityIdDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try{
            DiscountDto data = discountService.getById(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        }
    }
    @PostMapping(value = "/updateStatus")
    public ResponseEntity<ResponseDto<Boolean>> updateStatus(
            @Valid @RequestBody EntityIdDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try{
            Boolean data = discountService.updateStatus(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        }
    }
    @PostMapping(value = "/getDiscountDropDown")
    public ResponseEntity<ResponseDto<List<DiscountValueDto>>> getDiscountDropDown(
            @Valid @RequestBody EntityTypeDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try{
            List<DiscountValueDto> data = discountService.getDiscountDropDown(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }
    @GetMapping(value = "/getAllDiscounts")
    public ResponseEntity<ResponseDto<List<ViewDiscountDto>>> getAllDiscounts() {
        try{
            List<ViewDiscountDto> data = discountService.getAllDiscounts();
            return new ResponseEntity<>(new ResponseDto<>(data,null), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }
}
