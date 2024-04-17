package com.projectx.microservice.paymentservice.feignconfig;


import com.projectx.microservice.payloads.EntityIdDto;
import com.projectx.microservice.payloads.ResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service",url = "http://localhost:9096")
public interface OrderFeignClient {

    @PostMapping("/api/orders/updateOrderStatus")
    @CircuitBreaker(name = "order-service",fallbackMethod = "getDummyCall")
    ResponseEntity<ResponseDto<Boolean>> updateOrderStatus(@RequestBody EntityIdDto dto);

    public default ResponseEntity<ResponseDto<Boolean>> getDummyCall(Throwable throwable) {
        String message = "Order Service is temporary down wait for sometimes!!";
        return new ResponseEntity<>(new ResponseDto<>(null,message), HttpStatus.OK);
    }
}
