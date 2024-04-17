package com.projectx.microservice.categoryservice.controller;

import com.projectx.microservice.categoryservice.entity.ProductCategory;
import com.projectx.microservice.categoryservice.payloads.CategoryDto;
import com.projectx.microservice.categoryservice.payloads.ViewCategoryDto;
import com.projectx.microservice.categoryservice.service.ProductCategoryService;
import com.projectx.microservice.exceptions.AlreadyExistException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.payloads.EntityIdAndValueDto;
import com.projectx.microservice.payloads.EntityIdDto;
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
@RequestMapping("/api/category")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ErrorHandlerComponent errorHandler;

    @PostMapping("/addCategory")
    public ResponseEntity<ResponseDto<Boolean>> addUpdate(@Valid @RequestBody CategoryDto dto, BindingResult result) {
        if(result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try{
            Boolean data = productCategoryService.addUpdate(dto);
            return new ResponseEntity<ResponseDto<Boolean>>(new ResponseDto<Boolean>(data,null), HttpStatus.CREATED);
        }catch(ResourceNotFoundException | AlreadyExistException e){
            return errorHandler.handleError(e);
        }
    }

    @PostMapping("/getByIdCategory")
    public ResponseEntity<ResponseDto<CategoryDto>> getByIdCategory(@Valid @RequestBody EntityIdDto dto,BindingResult result) {
        if(result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            CategoryDto data = productCategoryService.getById(dto);
            return new ResponseEntity<ResponseDto<CategoryDto>>(new ResponseDto<CategoryDto>(data,null), HttpStatus.OK);
        }catch(ResourceNotFoundException e){
            return errorHandler.handleError(e);
        }
    }

    @GetMapping("/getCategoryDropDown")
    public ResponseEntity<ResponseDto<List<EntityIdAndValueDto>>> getCategoryDropDown() {
        try {
            List<EntityIdAndValueDto> data = productCategoryService.getDropDown();
            return new ResponseEntity<>(new ResponseDto<>(data,null),HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @GetMapping("/getAllProductCategories")
    public ResponseEntity<ResponseDto<List<ViewCategoryDto>>> getAllProductCategories() {
        try {
            List<ViewCategoryDto> data = productCategoryService.getAllProductCategories();
            return new ResponseEntity<>(new ResponseDto<>(data,null),HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping("/getProductCategory")
    public ResponseEntity<ResponseDto<ProductCategory>> getProductCategory(@Valid @RequestBody EntityIdDto dto,BindingResult result) {
        if(result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            ProductCategory data = productCategoryService.getProductCategory(dto);
            return new ResponseEntity<ResponseDto<ProductCategory>>(new ResponseDto<ProductCategory>(data,null), HttpStatus.OK);
        }catch(Exception e){
            return errorHandler.handleError(e);
        }
    }
}
