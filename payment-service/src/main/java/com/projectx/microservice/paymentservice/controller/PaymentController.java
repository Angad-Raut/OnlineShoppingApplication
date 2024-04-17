package com.projectx.microservice.paymentservice.controller;

import com.projectx.microservice.exceptions.AlreadyExistException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.payloads.PaymentAmountDto;
import com.projectx.microservice.payloads.ResponseDto;
import com.projectx.microservice.paymentservice.payloads.PaymentDto;
import com.projectx.microservice.paymentservice.service.PaymentService;
import com.projectx.microservice.utils.ErrorHandlerComponent;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ErrorHandlerComponent errorHandler;

    @PostMapping("/savePaymentDetails")
    public ResponseEntity<ResponseDto<Long>> savePaymentDetails(@Valid @RequestBody PaymentAmountDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Long paymentId = paymentService.savePaymentDetails(dto);
            return new ResponseEntity<ResponseDto<Long>>(new ResponseDto<Long>(paymentId,null),HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping("/doOfflinePayment")
    public ResponseEntity<ResponseDto<Boolean>> doOfflinePayment(@Valid @RequestBody PaymentDto dto,BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean status = paymentService.doOfflinePayment(dto);
            return new ResponseEntity<ResponseDto<Boolean>>(new ResponseDto<Boolean>(status,null),HttpStatus.OK);
        } catch (ResourceNotFoundException | AlreadyExistException e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping("/doOnlinePayment")
    public ResponseEntity<ResponseDto<String>> doOnlinePayment(@Valid @RequestBody PaymentDto dto,BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            String orderId = paymentService.doOnlinePayment(dto);
            return new ResponseEntity<ResponseDto<String>>(new ResponseDto<String>(orderId,null),HttpStatus.OK);
        } catch (ResourceNotFoundException | AlreadyExistException e) {
            return errorHandler.handleError(e);
        }
    }
}
