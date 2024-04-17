package com.projectx.microservice.orderservice.feignconfig;

import com.projectx.microservice.payloads.EntityIdDto;
import com.projectx.microservice.payloads.ProductInfoDto;
import com.projectx.microservice.payloads.ResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service",url = "http://localhost:9093")
public interface ProductFeignClient {

    @PostMapping("/api/product/getProductInfoByProduct")
    @CircuitBreaker(name = "product-service",fallbackMethod = "getDummyProductInfo")
    public ResponseEntity<ResponseDto<ProductInfoDto>> getProductInfoByProduct(@RequestBody EntityIdDto dto);

    @PostMapping("/api/product/isVerifiedProduct")
    @CircuitBreaker(name = "product-service",fallbackMethod = "getDummyIsVerifiedProduct")
    public ResponseEntity<ResponseDto<Boolean>> isVerifiedProduct(@RequestBody EntityIdDto dto);

    public default ResponseEntity<ResponseDto<ProductInfoDto>> getDummyProductInfo(Throwable throwable) {
        String message = "Product Service temporary down wait for sometimes!!";
        return new ResponseEntity<>(new ResponseDto<>(null,message), HttpStatus.OK);
    }

    public default ResponseEntity<ResponseDto<Boolean>> getDummyIsVerifiedProduct(Throwable throwable) {
        String message = "Product Service temporary down wait for sometimes!!";
        return new ResponseEntity<>(new ResponseDto<>(null,message), HttpStatus.OK);
    }
}
