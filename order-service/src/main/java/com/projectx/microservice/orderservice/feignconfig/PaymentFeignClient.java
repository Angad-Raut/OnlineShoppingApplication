package com.projectx.microservice.orderservice.feignconfig;

import com.projectx.microservice.payloads.PaymentAmountDto;
import com.projectx.microservice.payloads.ResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service",url = "http://localhost:9097")
public interface PaymentFeignClient {

    @PostMapping("/api/payment/savePaymentDetails")
    @CircuitBreaker(name = "payment-service",fallbackMethod = "getDummyCall")
    public ResponseEntity<ResponseDto<Long>> savePaymentDetails(@RequestBody PaymentAmountDto dto);

    public default ResponseEntity<ResponseDto<Long>> getDummyCall(Throwable throwable) {
        String message = "Payment Service temporary down wait for sometimes!!";
        return new ResponseEntity<>(new ResponseDto<>(null,message), HttpStatus.OK);
    }
}
