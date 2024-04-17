package com.projectx.microservice.inventoryservice.controller;

import com.projectx.microservice.exceptions.AlreadyExistException;
import com.projectx.microservice.exceptions.ProductNotVerifiedException;
import com.projectx.microservice.exceptions.ProductQuantityNotExistException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.payloads.EntityIdAndTypeDto;
import com.projectx.microservice.payloads.EntityIdDto;
import com.projectx.microservice.payloads.ResponseDto;
import com.projectx.microservice.inventoryservice.payloads.InventoryDto;
import com.projectx.microservice.inventoryservice.payloads.ViewStockDto;
import com.projectx.microservice.inventoryservice.service.InventoryService;
import com.projectx.microservice.utils.ErrorHandlerComponent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventoryStock")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ErrorHandlerComponent errorHandler;

    @PostMapping("/addStock")
    public ResponseEntity<ResponseDto<Boolean>> addStock(@Valid @RequestBody InventoryDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = inventoryService.addStock(dto);
            return new ResponseEntity<ResponseDto<Boolean>>(new ResponseDto<Boolean>(data,null), HttpStatus.CREATED);
        } catch (ResourceNotFoundException | AlreadyExistException | ProductNotVerifiedException e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping("/getInventoryStock")
    public ResponseEntity<ResponseDto<InventoryDto>> getInventoryStock(@Valid @RequestBody EntityIdDto dto,BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            InventoryDto data = inventoryService.getById(dto);
            return new ResponseEntity<ResponseDto<InventoryDto>>(new ResponseDto<InventoryDto>(data,null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        }
    }

    @GetMapping("/getAllProductsInventoryStock")
    public ResponseEntity<ResponseDto<List<ViewStockDto>>> getAllProductsInventoryStock() {
        try {
            List<ViewStockDto> data = inventoryService.getAllProductsStocks();
            return new ResponseEntity<>(new ResponseDto<>(data,null),HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping("/isProductInStock")
    public ResponseEntity<ResponseDto<Boolean>> isProductInStock(@Valid @RequestBody EntityIdAndTypeDto dto,BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = inventoryService.isProductInStock(dto);
            return new ResponseEntity<ResponseDto<Boolean>>(new ResponseDto<Boolean>(data,null), HttpStatus.OK);
        } catch (ProductQuantityNotExistException e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping("/updateStockQuantity")
    public ResponseEntity<ResponseDto<Boolean>> updateStockQuantity(@Valid @RequestBody EntityIdAndTypeDto dto,BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = inventoryService.updateStockQuantity(dto);
            return new ResponseEntity<ResponseDto<Boolean>>(new ResponseDto<Boolean>(data,null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping("/updateStockQuantityForAdmin")
    public ResponseEntity<ResponseDto<Boolean>> updateStockQuantityForAdmin(@Valid @RequestBody EntityIdAndTypeDto dto,BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = inventoryService.updateStockQuantityForAdmin(dto);
            return new ResponseEntity<ResponseDto<Boolean>>(new ResponseDto<Boolean>(data,null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        }
    }
    @PostMapping("/isInventoryExist")
    public ResponseEntity<ResponseDto<Boolean>> isInventoryExist(@Valid @RequestBody EntityIdDto dto,BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = inventoryService.isInventoryExist(dto);
            return new ResponseEntity<ResponseDto<Boolean>>(new ResponseDto<Boolean>(data,null), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }
}
