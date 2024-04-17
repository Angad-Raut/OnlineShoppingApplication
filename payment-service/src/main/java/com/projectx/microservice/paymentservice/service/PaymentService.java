package com.projectx.microservice.paymentservice.service;

import com.projectx.microservice.exceptions.AlreadyExistException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.payloads.PaymentAmountDto;
import com.projectx.microservice.paymentservice.payloads.PaymentDto;

public interface PaymentService {
    Long savePaymentDetails(PaymentAmountDto dto);
    Boolean doOfflinePayment(PaymentDto dto)throws ResourceNotFoundException,AlreadyExistException;
    String doOnlinePayment(PaymentDto dto)throws ResourceNotFoundException,AlreadyExistException;
}
