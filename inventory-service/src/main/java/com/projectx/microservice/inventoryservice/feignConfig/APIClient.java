package com.projectx.microservice.inventoryservice.feignConfig;

import com.projectx.microservice.payloads.EntityIdDto;
import com.projectx.microservice.payloads.ResponseDto;
import com.projectx.microservice.productservice.entity.ProductDetails;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient(value = "product-service",url = "http://localhost:9093")
public interface APIClient {

    @PostMapping("/api/product/getProductDetails")
    @CircuitBreaker(name = "product-service",fallbackMethod = "getDummyProductDetails")
    public ResponseEntity<ResponseDto<ProductDetails>> getProductDetails(@Valid @RequestBody EntityIdDto dto);

    @PostMapping("/api/product/isVerifiedProduct")
    @CircuitBreaker(name = "product-service",fallbackMethod = "getDummyIsVerifiedProduct")
    public ResponseEntity<ResponseDto<Boolean>> isVerifiedProduct(@Valid @RequestBody EntityIdDto dto);

    public default ResponseEntity<ResponseDto<ProductDetails>> getDummyProductDetails(Throwable throwable){
        String message = "Product Service temporary down wait for sometimes!!";
        return new ResponseEntity<>(new ResponseDto<>(null,message), HttpStatus.OK);
    }

    public default ResponseEntity<ResponseDto<Boolean>> getDummyIsVerifiedProduct(Throwable throwable){
        String message = "Product Service temporary down wait for sometimes!!";
        return new ResponseEntity<>(new ResponseDto<>(null,message), HttpStatus.OK);
    }
}
