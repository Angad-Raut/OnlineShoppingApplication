package com.projectx.microservice.orderservice.feignconfig;

import com.projectx.microservice.payloads.EntityIdAndTypeDto;
import com.projectx.microservice.payloads.ResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service",url = "http://localhost:9092")
public interface InventoryStockFeignClient {

    @PostMapping("/api/inventoryStock/isProductInStock")
    @CircuitBreaker(name = "inventory-service",fallbackMethod = "getDefaultCall")
    public ResponseEntity<ResponseDto<Boolean>> isProductInStock(@RequestBody EntityIdAndTypeDto dto);

    @PostMapping("/api/inventoryStock/updateStockQuantity")
    @CircuitBreaker(name = "inventory-service",fallbackMethod = "getDefaultCall")
    public ResponseEntity<ResponseDto<Boolean>> updateStockQuantity(@RequestBody EntityIdAndTypeDto dto);

    public default ResponseEntity<ResponseDto<Boolean>> getDefaultCall(Throwable throwable) {
        String message = "Inventory Service temporary down wait for sometimes!!";
        return new ResponseEntity<>(new ResponseDto<>(null,message), HttpStatus.OK);
    }
}
