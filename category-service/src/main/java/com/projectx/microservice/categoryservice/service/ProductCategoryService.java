package com.projectx.microservice.categoryservice.service;

import com.projectx.microservice.categoryservice.entity.ProductCategory;
import com.projectx.microservice.categoryservice.payloads.CategoryDto;
import com.projectx.microservice.categoryservice.payloads.ViewCategoryDto;
import com.projectx.microservice.exceptions.AlreadyExistException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.payloads.EntityIdAndValueDto;
import com.projectx.microservice.payloads.EntityIdDto;

import java.util.List;

public interface ProductCategoryService {
    Boolean addUpdate(CategoryDto Dto)throws ResourceNotFoundException, AlreadyExistException;
    CategoryDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    List<EntityIdAndValueDto> getDropDown();
    List<ViewCategoryDto> getAllProductCategories();
    ProductCategory getProductCategory(EntityIdDto dto);
}
