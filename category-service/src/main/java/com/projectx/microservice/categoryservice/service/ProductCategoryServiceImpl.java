package com.projectx.microservice.categoryservice.service;

import com.projectx.microservice.categoryservice.entity.ProductCategory;
import com.projectx.microservice.categoryservice.payloads.CategoryDto;
import com.projectx.microservice.categoryservice.payloads.ViewCategoryDto;
import com.projectx.microservice.categoryservice.repository.ProductCategoryRepository;
import com.projectx.microservice.exceptions.AlreadyExistException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.payloads.EntityIdAndValueDto;
import com.projectx.microservice.payloads.EntityIdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    private static final String CATEGORY_NOT_FOUND="Product category not found in the system!!";
    private static final String CATEGORY_EXIST="Product category already exists in the system!!";

    @Override
    public Boolean addUpdate(CategoryDto dto) throws ResourceNotFoundException, AlreadyExistException {
        ProductCategory productCategory = null;
        if (dto.getId()==null) {
               isCategoryExist(dto.getCategoryName());
               productCategory = ProductCategory.builder()
                       .categoryName(dto.getCategoryName())
                       .description(dto.getDescription())
                       .insertedTime(new Date())
                       .updatedTime(new Date())
                       .status(true)
                       .build();
        } else {
            productCategory = productCategoryRepository.getById(dto.getId());
            if (!dto.getCategoryName().equals(productCategory.getCategoryName())) {
                isCategoryExist(dto.getCategoryName());
                productCategory.setCategoryName(dto.getCategoryName());
            }
            if (!dto.getDescription().equals(productCategory.getDescription())) {
                productCategory.setDescription(dto.getDescription());
            }
            productCategory.setUpdatedTime(new Date());
        }
        try{
            return productCategoryRepository.save(productCategory)!=null?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (AlreadyExistException e) {
            throw new AlreadyExistException(e.getMessage());
        }
    }

    @Override
    public CategoryDto getById(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            ProductCategory productCategory = productCategoryRepository.getById(dto.getEntityId());
            if (productCategory==null) {
                throw new ResourceNotFoundException(CATEGORY_NOT_FOUND);
            }
            return CategoryDto.builder()
                    .id(productCategory.getId())
                    .categoryName(productCategory.getCategoryName())
                    .description(productCategory.getDescription())
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<EntityIdAndValueDto> getDropDown() {
        List<ProductCategory> fetchList = productCategoryRepository.findAll();
        return !fetchList.isEmpty()?fetchList.stream()
                .filter(ProductCategory::getStatus)
                .map(data -> EntityIdAndValueDto.builder()
                        .entityId(data.getId())
                        .entityValue(data.getCategoryName())
                        .build())
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public List<ViewCategoryDto> getAllProductCategories() {
        List<ProductCategory> fetchList = productCategoryRepository.findAll();
        AtomicInteger index = new AtomicInteger(0);
        return !fetchList.isEmpty()?fetchList.stream()
                .map(data -> ViewCategoryDto.builder()
                        .srNo(index.incrementAndGet())
                        .categoryId(data.getId())
                        .categoryName(data.getCategoryName())
                        .categoryDescription(data.getDescription())
                        .categoryStatus(data.getStatus()?"Enabled":"Disabled")
                        .build())
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public ProductCategory getProductCategory(EntityIdDto dto) {
        return productCategoryRepository.getById(dto.getEntityId());
    }

    private void isCategoryExist(String category) {
        if (productCategoryRepository.existsByCategoryName(category)) {
            throw new AlreadyExistException(CATEGORY_EXIST);
        }
    }
}
