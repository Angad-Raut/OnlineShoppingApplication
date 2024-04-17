package com.projectx.microservice.productservice.controller;

import com.projectx.microservice.exceptions.AlreadyExistException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.payloads.EntityIdAndValueDto;
import com.projectx.microservice.payloads.EntityIdDto;
import com.projectx.microservice.payloads.ProductInfoDto;
import com.projectx.microservice.payloads.ResponseDto;
import com.projectx.microservice.productservice.entity.ProductDetails;
import com.projectx.microservice.productservice.payloads.EditProductDto;
import com.projectx.microservice.productservice.payloads.ProductDropDownDto;
import com.projectx.microservice.productservice.payloads.ProductDto;
import com.projectx.microservice.productservice.payloads.ViewProductDto;
import com.projectx.microservice.productservice.service.ProductService;
import com.projectx.microservice.utils.ErrorHandlerComponent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ErrorHandlerComponent errorHandler;

    @PostMapping("/addProduct")
    public ResponseEntity<ResponseDto<Boolean>> addProduct(@Valid @ModelAttribute ProductDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = productService.addProduct(dto);
            return new ResponseEntity<ResponseDto<Boolean>>(new ResponseDto<Boolean>(data,null), HttpStatus.CREATED);
        } catch (ResourceNotFoundException | AlreadyExistException | IOException e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping("/getProductById")
    public ResponseEntity<ResponseDto<EditProductDto>> getProductById(@Valid @RequestBody EntityIdDto dto,BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            EditProductDto data = productService.getById(dto);
            return new ResponseEntity<ResponseDto<EditProductDto>>(new ResponseDto<EditProductDto>(data,null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping("/updateProductStatusById")
    public ResponseEntity<ResponseDto<Boolean>> updateProductStatusById(@Valid @RequestBody EntityIdDto dto,BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = productService.updateProductStatus(dto);
            return new ResponseEntity<ResponseDto<Boolean>>(new ResponseDto<Boolean>(data,null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping("/getProductDropDown")
    public ResponseEntity<ResponseDto<List<EntityIdAndValueDto>>> getProductDropDown(@Valid @RequestBody EntityIdDto dto,BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            List<EntityIdAndValueDto> data = productService.getProductDropDown(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null),HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @GetMapping("/getAllProductList")
    public ResponseEntity<ResponseDto<List<ViewProductDto>>> getAllProductList() {
        try {
            List<ViewProductDto> data = productService.getAllProducts();
            return new ResponseEntity<>(new ResponseDto<>(data,null),HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping("/getProductDetails")
    public ResponseEntity<ResponseDto<ProductDetails>> getProductDetails(@Valid @RequestBody EntityIdDto dto,BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            ProductDetails data = productService.getProductDetails(dto);
            return new ResponseEntity<ResponseDto<ProductDetails>>(new ResponseDto<ProductDetails>(data,null), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping("/isVerifiedProduct")
    public ResponseEntity<ResponseDto<Boolean>> isVerifiedProduct(@Valid @RequestBody EntityIdDto dto,BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = productService.isVerifiedProduct(dto);
            return new ResponseEntity<ResponseDto<Boolean>>(new ResponseDto<Boolean>(data,null), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping("/getProductInfoByProduct")
    public ResponseEntity<ResponseDto<ProductInfoDto>> getProductInfoByProduct(@Valid @RequestBody EntityIdDto dto,BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            ProductInfoDto data = productService.getProductInfoByProduct(dto);
            return new ResponseEntity<ResponseDto<ProductInfoDto>>(new ResponseDto<ProductInfoDto>(data,null), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping("/getProductDropDownByCategory")
    public ResponseEntity<ResponseDto<List<ProductDropDownDto>>> getProductDropDownByCategory(@Valid @RequestBody EntityIdDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            List<ProductDropDownDto> data = productService.getProductDropDownByCategory(dto);
            return new ResponseEntity<ResponseDto<List<ProductDropDownDto>>>(new ResponseDto<List<ProductDropDownDto>>(data,null), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }
}
