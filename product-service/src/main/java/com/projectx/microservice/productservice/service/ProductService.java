package com.projectx.microservice.productservice.service;

import com.projectx.microservice.exceptions.AlreadyExistException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.payloads.EntityIdAndValueDto;
import com.projectx.microservice.payloads.EntityIdDto;
import com.projectx.microservice.payloads.ProductInfoDto;
import com.projectx.microservice.productservice.entity.ProductDetails;
import com.projectx.microservice.productservice.payloads.EditProductDto;
import com.projectx.microservice.productservice.payloads.ProductDropDownDto;
import com.projectx.microservice.productservice.payloads.ProductDto;
import com.projectx.microservice.productservice.payloads.ViewProductDto;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    Boolean addProduct(ProductDto dto) throws ResourceNotFoundException, AlreadyExistException, IOException;
    EditProductDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    List<EntityIdAndValueDto> getProductDropDown(EntityIdDto dto);
    List<ViewProductDto> getAllProducts();
    Boolean updateProductStatus(EntityIdDto dto)throws ResourceNotFoundException;
    ProductDetails getProductDetails(EntityIdDto dto);
    Boolean isVerifiedProduct(EntityIdDto dto);
    ProductInfoDto getProductInfoByProduct(EntityIdDto dto);
    List<ProductDropDownDto> getProductDropDownByCategory(EntityIdDto dto);
}
