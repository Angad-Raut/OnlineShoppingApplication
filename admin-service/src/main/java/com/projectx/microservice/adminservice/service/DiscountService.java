package com.projectx.microservice.adminservice.service;

import com.projectx.microservice.adminservice.payloads.DiscountDto;
import com.projectx.microservice.adminservice.payloads.DiscountValueDto;
import com.projectx.microservice.adminservice.payloads.ViewDiscountDto;
import com.projectx.microservice.exceptions.AlreadyExistException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.payloads.EntityIdDto;
import com.projectx.microservice.payloads.EntityTypeDto;

import java.util.List;

public interface DiscountService {
    Boolean addUpdateDiscount(DiscountDto dto)throws ResourceNotFoundException, AlreadyExistException;
    DiscountDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    Boolean updateStatus(EntityIdDto dto)throws ResourceNotFoundException;
    List<ViewDiscountDto> getAllDiscounts();
    List<DiscountValueDto> getDiscountDropDown(EntityTypeDto dto);
}
