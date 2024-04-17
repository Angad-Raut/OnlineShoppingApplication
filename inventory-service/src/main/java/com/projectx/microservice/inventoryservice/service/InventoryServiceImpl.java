package com.projectx.microservice.inventoryservice.service;

import com.projectx.microservice.categoryservice.entity.ProductCategory;
import com.projectx.microservice.exceptions.AlreadyExistException;
import com.projectx.microservice.exceptions.ProductNotVerifiedException;
import com.projectx.microservice.exceptions.ProductQuantityNotExistException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.inventoryservice.feignConfig.CategoryClient;
import com.projectx.microservice.payloads.EntityIdAndTypeDto;
import com.projectx.microservice.payloads.EntityIdDto;
import com.projectx.microservice.payloads.ResponseDto;
import com.projectx.microservice.inventoryservice.entity.InventoryStock;
import com.projectx.microservice.inventoryservice.feignConfig.APIClient;
import com.projectx.microservice.inventoryservice.payloads.InventoryDto;
import com.projectx.microservice.inventoryservice.payloads.ViewStockDto;
import com.projectx.microservice.inventoryservice.repository.InventoryRepository;
import com.projectx.microservice.productservice.entity.ProductDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private APIClient apiClient;

    @Autowired
    private CategoryClient categoryClient;

    private static final String INVENTORY_STOCK_NOT_FOUND="Product inventory stock not found!!";

    private static final String PRODUCT_NOT_VERIFIED="Product not verified please check with admin!!";

    private static final String PRODUCT_QANTITY_EXCEEDS="Product quantity exceeds please contact with admin!!";

    @Override
    public Boolean addStock(InventoryDto dto) throws ResourceNotFoundException,
            AlreadyExistException,ProductNotVerifiedException {
        InventoryStock inventoryStock=null;
        if (dto.getId()==null) {
                ResponseEntity<ResponseDto<ProductDetails>> response = apiClient.getProductDetails(new EntityIdDto(dto.getProductId()));
                ResponseEntity<ResponseDto<ProductCategory>> response1 = categoryClient.getProductCategory(new EntityIdDto(dto.getCategoryId()));
                if (response.getBody().getResult()==null && response.getBody().getErrorMessage()!=null) {
                    throw new ResourceNotFoundException(response.getBody().getErrorMessage());
                }
                if (response1.getBody().getResult()==null && response1.getBody().getErrorMessage()!=null) {
                    throw new ResourceNotFoundException(response1.getBody().getErrorMessage());
                }
                ProductDetails productDetails = Objects.requireNonNull(response.getBody()).getResult();
                ProductCategory productCategory = Objects.requireNonNull(response1.getBody().getResult());
                isVerifiedProduct(productDetails.getProductId());
                inventoryStock = InventoryStock.builder()
                        .categoryId(productCategory.getId())
                        .productId(productDetails.getProductId())
                        .totalCount(dto.getQuantity())
                        .remainingCount(dto.getQuantity())
                        .reservedCount(0)
                        .insertedTime(new Date())
                        .updatedTime(new Date())
                        .status(true)
                        .build();
        } else {
                inventoryStock = inventoryRepository.getById(dto.getId());
                if (inventoryStock==null) {
                    throw new ResourceNotFoundException(INVENTORY_STOCK_NOT_FOUND);
                }
                if (!dto.getCategoryId().equals(inventoryStock.getCategoryId())) {
                    ResponseEntity<ResponseDto<ProductCategory>> response1 = categoryClient.getProductCategory(new EntityIdDto(dto.getCategoryId()));
                    if (response1.getBody().getResult()==null && response1.getBody().getErrorMessage()!=null) {
                        throw new ResourceNotFoundException(response1.getBody().getErrorMessage());
                    }
                    inventoryStock.setCategoryId(dto.getCategoryId());
                }
                if (!dto.getProductId().equals(inventoryStock.getProductId())) {
                    ResponseEntity<ResponseDto<ProductDetails>> response = apiClient.getProductDetails(new EntityIdDto(dto.getProductId()));
                    if (response.getBody().getResult()==null && response.getBody().getErrorMessage()!=null) {
                        throw new ResourceNotFoundException(response.getBody().getErrorMessage());
                    }
                    ProductDetails productDetails = Objects.requireNonNull(response.getBody().getResult());
                    isVerifiedProduct(productDetails.getProductId());
                    inventoryStock.setProductId(productDetails.getProductId());
                }
                if (!dto.getQuantity().equals(inventoryStock.getTotalCount())) {
                    Integer totalCount = inventoryStock.getTotalCount()!=null?inventoryStock.getTotalCount():0;
                    inventoryStock.setTotalCount(totalCount+ dto.getQuantity());
                    Integer remainingCount = inventoryStock.getRemainingCount()!=null?inventoryStock.getRemainingCount():0;
                    inventoryStock.setRemainingCount(remainingCount+dto.getQuantity());
                }
                inventoryStock.setUpdatedTime(new Date());
        }
        try {
             return inventoryRepository.save(inventoryStock)!=null?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (AlreadyExistException e){
            throw new AlreadyExistException(e.getMessage());
        }
    }

    @Override
    public InventoryDto getById(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            InventoryStock inventoryStock = inventoryRepository.getById(dto.getEntityId());
            if (inventoryStock==null) {
                throw new ResourceNotFoundException(INVENTORY_STOCK_NOT_FOUND);
            }
            return InventoryDto.builder()
                    .id(inventoryStock.getId())
                    .categoryId(inventoryStock.getCategoryId())
                    .productId(inventoryStock.getProductId())
                    .quantity(inventoryStock.getTotalCount())
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<ViewStockDto> getAllProductsStocks() {
        List<InventoryStock> fetchList = inventoryRepository.findAll();
        AtomicInteger index = new AtomicInteger(0);
        return !fetchList.isEmpty()?fetchList.stream()
                .map(data -> ViewStockDto.builder()
                        .srNo(index.incrementAndGet())
                        .inventoryId(data.getId())
                        .categoryName(getCategoryName(data.getCategoryId()))
                        .productName(getProductName(data.getProductId()))
                        .totalCount(data.getTotalCount())
                        .remainingCount(data.getRemainingCount())
                        .reservedCount(data.getReservedCount())
                        .build())
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public Boolean isProductInStock(EntityIdAndTypeDto dto) throws ProductQuantityNotExistException {
        try {
            InventoryStock stock = inventoryRepository.findInventoryStockByProductId(dto.getEntityId());
            if (stock.getRemainingCount()>dto.getEntityType()) {
                return true;
            } else {
                throw new ProductQuantityNotExistException(PRODUCT_QANTITY_EXCEEDS);
            }
        } catch (ProductQuantityNotExistException e) {
            throw new ProductQuantityNotExistException(e.getMessage());
        }
    }

    @Override
    public Boolean updateStockQuantity(EntityIdAndTypeDto dto) throws ResourceNotFoundException {
        try {
            InventoryStock stock = inventoryRepository.findInventoryStockByProductId(dto.getEntityId());
            if (stock.getRemainingCount()>dto.getEntityType()) {
                Integer remainingCount = (stock.getRemainingCount()- dto.getEntityType());
                Integer reservedCount = (stock.getReservedCount()+ dto.getEntityType());
                Integer count = inventoryRepository.updateInventoryStock(stock.getProductId(),remainingCount,reservedCount);
                return count==1?true:false;
            } else {
                throw new ProductQuantityNotExistException(PRODUCT_QANTITY_EXCEEDS);
            }
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public Boolean updateStockQuantityForAdmin(EntityIdAndTypeDto dto) throws ResourceNotFoundException {
        try {
            InventoryStock stock = inventoryRepository.findInventoryStockByProductId(dto.getEntityId());
            if (stock!=null) {
                Integer remainingCount = (stock.getRemainingCount() + dto.getEntityType());
                Integer totalCount = (stock.getTotalCount() + dto.getEntityType());
                Integer count = inventoryRepository.updateInventoryStockForAdmin(stock.getProductId(), remainingCount, totalCount);
                return count == 1 ? true : false;
            } else {
                throw new ResourceNotFoundException(INVENTORY_STOCK_NOT_FOUND);
            }
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public Boolean isInventoryExist(EntityIdDto dto) {
        return inventoryRepository.existsInventoryStockByProductId(dto.getEntityId())?true:false;
    }

    private Boolean isVerifiedProduct(Long productId) {
        ResponseEntity<ResponseDto<Boolean>> response = apiClient.isVerifiedProduct(new EntityIdDto(productId));
        Boolean isVerified = Objects.requireNonNull(response.getBody()).getResult();
        if (isVerified) {
            return true;
        } else {
            throw new ProductNotVerifiedException(PRODUCT_NOT_VERIFIED);
        }
    }
    private String getProductName(Long productId) {
        ResponseEntity<ResponseDto<ProductDetails>> response = apiClient.getProductDetails(new EntityIdDto(productId));
        ProductDetails productDetails = Objects.requireNonNull(response.getBody()).getResult();
        return productDetails!=null?productDetails.getProductName():"-";
    }
    private String getCategoryName(Long categoryId) {
        ResponseEntity<ResponseDto<ProductCategory>> response = categoryClient.getProductCategory(new EntityIdDto(categoryId));
        ProductCategory category = Objects.requireNonNull(response.getBody()).getResult();
        return category!=null?category.getCategoryName():"-";
    }
}
