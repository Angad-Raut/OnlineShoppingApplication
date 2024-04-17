package com.projectx.microservice.orderservice.controller;

import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.orderservice.payloads.OrderDto;
import com.projectx.microservice.orderservice.service.OrderService;
import com.projectx.microservice.payloads.EntityIdDto;
import com.projectx.microservice.payloads.ResponseDto;
import com.projectx.microservice.utils.ErrorHandlerComponent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ErrorHandlerComponent errorHandler;

    @PostMapping("/placeOrder")
    public ResponseEntity<ResponseDto<Boolean>> placeOrder(@Valid @RequestBody OrderDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean status = orderService.placeOrder(dto);
            return new ResponseEntity<ResponseDto<Boolean>>(new ResponseDto<Boolean>(status,null),HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping("/updateOrderStatus")
    public ResponseEntity<ResponseDto<Boolean>> updateOrderStatus(@Valid @RequestBody EntityIdDto dto,BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean status = orderService.updateOrderStatus(dto);
            return new ResponseEntity<ResponseDto<Boolean>>(new ResponseDto<Boolean>(status,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        }
    }
}
