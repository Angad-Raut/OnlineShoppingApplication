package com.projectx.microservice.inventoryservice.service;

import com.projectx.microservice.exceptions.AlreadyExistException;
import com.projectx.microservice.exceptions.ProductNotVerifiedException;
import com.projectx.microservice.exceptions.ProductQuantityNotExistException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.payloads.EntityIdAndTypeDto;
import com.projectx.microservice.payloads.EntityIdDto;
import com.projectx.microservice.inventoryservice.payloads.InventoryDto;
import com.projectx.microservice.inventoryservice.payloads.ViewStockDto;

import java.util.List;

public interface InventoryService {
    Boolean addStock(InventoryDto dto)throws ResourceNotFoundException,
            AlreadyExistException, ProductNotVerifiedException;
    InventoryDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    List<ViewStockDto> getAllProductsStocks();
    Boolean isProductInStock(EntityIdAndTypeDto dto)throws ProductQuantityNotExistException;
    Boolean updateStockQuantity(EntityIdAndTypeDto dto) throws ResourceNotFoundException;
    Boolean updateStockQuantityForAdmin(EntityIdAndTypeDto dto) throws ResourceNotFoundException;
    Boolean isInventoryExist(EntityIdDto dto);
}
