package com.projectx.microservice.adminservice.service;

import com.projectx.microservice.adminservice.entity.DiscountDetails;
import com.projectx.microservice.adminservice.payloads.DiscountDto;
import com.projectx.microservice.adminservice.payloads.DiscountValueDto;
import com.projectx.microservice.adminservice.payloads.ViewDiscountDto;
import com.projectx.microservice.adminservice.repository.DiscountRepository;
import com.projectx.microservice.exceptions.AlreadyExistException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.payloads.EntityIdDto;
import com.projectx.microservice.payloads.EntityTypeDto;
import com.projectx.microservice.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class DiscountServiceImpl implements DiscountService{

    @Autowired
    private DiscountRepository discountRepository;

    @Override
    public Boolean addUpdateDiscount(DiscountDto dto) throws ResourceNotFoundException, AlreadyExistException {
        DiscountDetails discountDetails = null;
        if (dto.getId()==null) {
            isDiscountExist(dto.getDiscountType(),dto.getDiscount());
            discountDetails = DiscountDetails.builder()
                    .discountType(dto.getDiscountType())
                    .discount(dto.getDiscount())
                    .updatedTime(new Date())
                    .insertedTime(new Date())
                    .status(true)
                    .build();
        } else {
            discountDetails = discountRepository.getById(dto.getId());
            if (discountDetails==null) {
                throw new ResourceNotFoundException(Constants.DISCOUNT_NOT_EXIST);
            }
            if (!dto.getDiscountType().equals(discountDetails.getDiscountType())) {
                discountDetails.setDiscountType(dto.getDiscountType());
            }
            if (!dto.getDiscount().equals(discountDetails.getDiscount())) {
                isDiscountExist(dto.getDiscountType(),dto.getDiscount());
                discountDetails.setDiscount(dto.getDiscount());
            }
            discountDetails.setUpdatedTime(new Date());
        }
        try {
            return discountRepository.save(discountDetails)!=null?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (AlreadyExistException e) {
            throw new AlreadyExistException(e.getMessage());
        }
    }

    @Override
    public DiscountDto getById(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            DiscountDetails discountDetails = discountRepository.getById(dto.getEntityId());
            if (discountDetails==null) {
                throw new ResourceNotFoundException(Constants.DISCOUNT_NOT_EXIST);
            }
            return DiscountDto.builder()
                    .id(discountDetails.getId())
                    .discountType(discountDetails.getDiscountType())
                    .discount(discountDetails.getDiscount())
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public Boolean updateStatus(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            DiscountDetails discountDetails = discountRepository.getById(dto.getEntityId());
            if (discountDetails==null) {
                throw new ResourceNotFoundException(Constants.DISCOUNT_NOT_EXIST);
            }
            Boolean status = discountDetails.getStatus()?false:true;
            Integer count = discountRepository.updateStatus(dto.getEntityId(),status);
            return count==1?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<ViewDiscountDto> getAllDiscounts() {
        List<DiscountDetails> fetchList = discountRepository.findAll();
        AtomicInteger index = new AtomicInteger(0);
        return !fetchList.isEmpty()?fetchList.stream()
                .map(data -> ViewDiscountDto.builder()
                        .srNo(index.incrementAndGet())
                        .id(data.getId())
                        .discountType(data.getDiscountType())
                        .discount(data.getDiscount())
                        .status(data.getStatus()?"Enable":"Disable")
                        .build())
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public List<DiscountValueDto> getDiscountDropDown(EntityTypeDto dto) {
        List<Double> fetchList = discountRepository.getDiscountByType(dto.getEntityType());
        return !fetchList.isEmpty()?fetchList.stream()
                .map(data -> DiscountValueDto.builder()
                        .discount(data)
                        .build())
                .collect(Collectors.toList()) : new ArrayList<>();
    }
    private void isDiscountExist(Integer discountType,Double discount) {
        if (discountRepository.existsDiscountDetailsByDiscountTypeAndDiscount(discountType,discount)) {
            throw new AlreadyExistException(Constants.DISCOUNT_EXIST);
        }
    }
}
