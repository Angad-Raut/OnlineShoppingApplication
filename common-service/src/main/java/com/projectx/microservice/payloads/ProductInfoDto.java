package com.projectx.microservice.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInfoDto {
    private Long productId;
    private String productName;
    private Double productPrice;
    private String productHsnCode;
    private Integer productTaxPercentage;
    private byte[] productImage;
}
