package com.projectx.microservice.productservice.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDropDownDto {
    private Long productId;
    private String productName;
    private Double productPrice;
    private byte[] productImage;
}
