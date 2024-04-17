package com.projectx.microservice.inventoryservice.feignConfig;

import com.projectx.microservice.categoryservice.entity.ProductCategory;
import com.projectx.microservice.payloads.EntityIdDto;
import com.projectx.microservice.payloads.ResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "category-service",url = "http://localhost:9090")
public interface CategoryClient {

    @PostMapping("/api/category/getProductCategory")
    @CircuitBreaker(name = "category-service",fallbackMethod = "getDummyCall")
    public ResponseEntity<ResponseDto<ProductCategory>> getProductCategory(@Valid @RequestBody EntityIdDto dto);

    public default ResponseEntity<ResponseDto<ProductCategory>> getDummyCall(Throwable throwable) {
        String message = "Product Category Service is temporary down wait for sometimes!!";
        return new ResponseEntity<>(new ResponseDto<>(null,message), HttpStatus.OK);
    }
}
