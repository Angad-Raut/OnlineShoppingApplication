package com.projectx.microservice.productservice.service;

import com.projectx.microservice.categoryservice.entity.ProductCategory;
import com.projectx.microservice.exceptions.AlreadyExistException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.payloads.EntityIdAndValueDto;
import com.projectx.microservice.payloads.EntityIdDto;
import com.projectx.microservice.payloads.ProductInfoDto;
import com.projectx.microservice.payloads.ResponseDto;
import com.projectx.microservice.productservice.entity.ProductDetails;
import com.projectx.microservice.productservice.entity.ProductFeatures;
import com.projectx.microservice.productservice.feignConfig.APIClient;
import com.projectx.microservice.productservice.feignConfig.CategoryAPI;
import com.projectx.microservice.productservice.payloads.*;
import com.projectx.microservice.productservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private APIClient apiClient;

    @Autowired
    private CategoryAPI categoryAPI;

    private static final String PRODUCT_NOT_FOUND="Product details not present in the system!!";
    private static final String PRODUCT_EXIST="Product already exists in the system!!";
    private static final Integer ADD_PRODUCT_STATUS=1;
    private static final Integer VERIFY_PRODUCT_STATUS=2;

    @Transactional
    @Override
    public Boolean addProduct(ProductDto dto) throws ResourceNotFoundException, AlreadyExistException, IOException {
        ProductDetails productDetails = null;
        if (dto.getProductId()==null) {
               isProductExists(dto.getProductName());
               ResponseEntity<ResponseDto<ProductCategory>> response = apiClient.getProductCategory(new EntityIdDto(dto.getCategoryId()));
               ProductCategory productCategory = Objects.requireNonNull(response.getBody()).getResult();
               productDetails = ProductDetails.builder()
                       .productName(dto.getProductName())
                       .productBrand(dto.getProductBrand())
                       .productPrice(dto.getProductPrice())
                       .productDescription(dto.getProductDescription())
                       .categoryId(productCategory.getId())
                       .productFeatures(setProductFeatures(dto.getProductFeatures()))
                       .productImage(dto.getProductImage().getBytes())
                       .productTaxPercentage(dto.getProductTaxPercentage())
                       .productHsnCode(dto.getProductHsnCode())
                       .insertedTime(new Date())
                       .updatedTime(new Date())
                       .productStatus(ADD_PRODUCT_STATUS)
                       .build();
        } else {
               productDetails = productRepository.getById(dto.getProductId());
               if (productDetails==null) {
                   throw new ResourceNotFoundException(PRODUCT_NOT_FOUND);
               }
               if (!dto.getProductName().equals(productDetails.getProductName())) {
                   isProductExists(dto.getProductName());
                   productDetails.setProductName(dto.getProductName());
               }
               if (!dto.getProductDescription().equals(productDetails.getProductDescription())) {
                   productDetails.setProductDescription(dto.getProductDescription());
               }
               if (!dto.getProductBrand().equals(productDetails.getProductBrand())) {
                   productDetails.setProductBrand(dto.getProductBrand());
               }
               if(!dto.getCategoryId().equals(productDetails.getCategoryId())) {
                   ResponseEntity<ResponseDto<ProductCategory>> response = apiClient.getProductCategory(new EntityIdDto(dto.getCategoryId()));
                   ProductCategory productCategory = Objects.requireNonNull(response.getBody()).getResult();
                   productDetails.setCategoryId(productCategory.getId());
               }
               if (!dto.getProductPrice().equals(productDetails.getProductPrice())) {
                   productDetails.setProductPrice(dto.getProductPrice());
               }
               if (!dto.getProductFeatures().equals(productDetails.getProductFeatures())) {
                   productDetails.getProductFeatures().clear();
                   productDetails.setProductFeatures(setProductFeatures(dto.getProductFeatures()));
               }
               if (!dto.getProductHsnCode().equals(productDetails.getProductHsnCode())) {
                   productDetails.setProductHsnCode(dto.getProductHsnCode());
               }
               if (!dto.getProductTaxPercentage().equals(productDetails.getProductTaxPercentage())) {
                   productDetails.setProductTaxPercentage(dto.getProductTaxPercentage());
               }
               if (dto.getProductImage()!=null && !Arrays.equals(dto.getProductImage().getBytes(), productDetails.getProductImage())) {
                   productDetails.setProductImage(dto.getProductImage().getBytes());
               }
               productDetails.setUpdatedTime(new Date());
        }
        try {
            return productRepository.save(productDetails)!=null?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (AlreadyExistException e) {
            throw new AlreadyExistException(e.getMessage());
        }
    }

    @Override
    public EditProductDto getById(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            ProductDetails productDetails = productRepository.getById(dto.getEntityId());
            if (productDetails==null) {
                throw new ResourceNotFoundException(PRODUCT_NOT_FOUND);
            }
            return EditProductDto.builder()
                    .productId(productDetails.getProductId())
                    .productName(productDetails.getProductName())
                    .productPrice(productDetails.getProductPrice())
                    .productDescription(productDetails.getProductDescription())
                    .categoryId(productDetails.getCategoryId())
                    .productBrand(productDetails.getProductBrand())
                    .productFeatures(setFeatures(productDetails.getProductFeatures()))
                    .productImage(productDetails.getProductImage())
                    .productHsnCode(productDetails.getProductHsnCode())
                    .productTaxPercentage(productDetails.getProductTaxPercentage())
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<EntityIdAndValueDto> getProductDropDown(EntityIdDto dto) {
        List<ProductDetails> fetchList = productRepository.findAll();
        return !fetchList.isEmpty()?fetchList.stream()
                .filter(s -> s.getProductStatus().equals(VERIFY_PRODUCT_STATUS)
                        && s.getCategoryId().equals(dto.getEntityId()))
                .map(data -> EntityIdAndValueDto.builder()
                        .entityId(data.getProductId())
                        .entityValue(data.getProductName())
                        .build())
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public List<ViewProductDto> getAllProducts() {
        List<ProductDetails> fetchList = productRepository.findAll();
        AtomicInteger index = new AtomicInteger(0);
        return !fetchList.isEmpty()?fetchList.stream()
                .map(data -> ViewProductDto.builder()
                        .srNo(index.incrementAndGet())
                        .productId(data.getProductId())
                        .productName(data.getProductName())
                        .productBrand(data.getProductBrand())
                        .productPrice(data.getProductPrice())
                        .status(data.getProductStatus().equals(ADD_PRODUCT_STATUS)?false:true)
                        .productDescription(data.getProductDescription())
                        .productCategory(getCategory(data.getCategoryId()))
                        .build())
                .collect(Collectors.toList()):new ArrayList<>();
    }

    @Override
    public Boolean updateProductStatus(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            ProductDetails productDetails = productRepository.getById(dto.getEntityId());
            if (productDetails==null) {
                throw new ResourceNotFoundException(PRODUCT_NOT_FOUND);
            }
            Integer count = productRepository.updateProductStatusById(productDetails.getProductId(),VERIFY_PRODUCT_STATUS);
            return count==1?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public ProductDetails getProductDetails(EntityIdDto dto) {
        return productRepository.getById(dto.getEntityId());
    }

    @Override
    public Boolean isVerifiedProduct(EntityIdDto dto) {
        return productRepository.getById(dto.getEntityId())
                .getProductStatus().equals(VERIFY_PRODUCT_STATUS)?true:false;
    }

    @Override
    public ProductInfoDto getProductInfoByProduct(EntityIdDto dto) {
        ProductDetails productDetails = productRepository.getById(dto.getEntityId());
        return productDetails!=null?ProductInfoDto.builder()
                .productId(productDetails.getProductId())
                .productName(productDetails.getProductName())
                .productPrice(productDetails.getProductPrice())
                .productHsnCode(productDetails.getProductHsnCode())
                .productTaxPercentage(productDetails.getProductTaxPercentage())
                .productImage(productDetails.getProductImage())
                .build() : new ProductInfoDto();
    }

    @Override
    public List<ProductDropDownDto> getProductDropDownByCategory(EntityIdDto dto) {
        List<ProductDetails> fetchList = productRepository.getProductByCategory(dto.getEntityId());
        return !fetchList.isEmpty()?fetchList.stream()
                .filter(s -> s.getProductStatus().equals(VERIFY_PRODUCT_STATUS)
                        && isInvExist(s.getProductId()))
                .map(data -> ProductDropDownDto.builder()
                        .productId(data.getProductId())
                        .productName(data.getProductName())
                        .productPrice(data.getProductPrice())
                        .productImage(data.getProductImage())
                        .build())
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    private void isProductExists(String productName) {
        if (productRepository.existsByProductName(productName)) {
            throw new AlreadyExistException(PRODUCT_EXIST);
        }
    }
    private Boolean isInvExist(Long productId) {
        ResponseEntity<ResponseDto<Boolean>> response = categoryAPI.isInventoryExist(new EntityIdDto(productId));
        return response.getBody()!=null && response.getBody().getResult()!=null?true:false;
    }
    private String getCategory(Long categoryId) {
        ResponseEntity<ResponseDto<ProductCategory>> response = apiClient.getProductCategory(new EntityIdDto(categoryId));
        ProductCategory productCategory = Objects.requireNonNull(response.getBody()).getResult();
        return productCategory!=null?productCategory.getCategoryName():"-";
    }
    private List<ProductFeatures> setProductFeatures(List<ProductFeatureDto> featureDtos) {
        return !featureDtos.isEmpty()?featureDtos.stream()
                .map(data -> ProductFeatures.builder()
                        .name(data.getFeatureName())
                        .value(data.getFeatureValue())
                        .build())
                .collect(Collectors.toList()):new ArrayList<>();
    }
    private List<ProductFeatureDto> setFeatures(List<ProductFeatures> features) {
        return !features.isEmpty()?features.stream()
                .map(data -> ProductFeatureDto.builder()
                        .featureName(data.getName())
                        .featureValue(data.getValue())
                        .build())
                .collect(Collectors.toList()):new ArrayList<>();
    }
}
