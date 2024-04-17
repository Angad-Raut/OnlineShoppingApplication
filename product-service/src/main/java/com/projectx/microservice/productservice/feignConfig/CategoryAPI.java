package com.projectx.microservice.productservice.feignConfig;
import com.projectx.microservice.payloads.EntityIdDto;
import com.projectx.microservice.payloads.ResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "inventory-service",url = "http://localhost:9092")
public interface CategoryAPI {

    @PostMapping("/api/inventoryStock/isInventoryExist")
    @CircuitBreaker(name = "inventory-service",fallbackMethod = "getDummyCall")
    public ResponseEntity<ResponseDto<Boolean>> isInventoryExist(@RequestBody EntityIdDto dto);

    public default ResponseEntity<ResponseDto<Boolean>> getDummyCall(Throwable throwable) {
        String message = "Inventory Service is temporary down wait for sometimes!!";
        return new ResponseEntity<>(new ResponseDto<>(null,message), HttpStatus.OK);
    }
}
