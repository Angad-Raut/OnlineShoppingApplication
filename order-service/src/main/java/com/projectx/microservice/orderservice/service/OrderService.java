package com.projectx.microservice.orderservice.service;

import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.orderservice.payloads.OrderDto;
import com.projectx.microservice.payloads.EntityIdDto;

public interface OrderService {
    Boolean placeOrder(OrderDto dto);
    Boolean updateOrderStatus(EntityIdDto dto)throws ResourceNotFoundException;
}
