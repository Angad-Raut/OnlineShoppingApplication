package com.projectx.microservice.productservice.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewProductDto {
    private Integer srNo;
    private Long productId;
    private String productName;
    private String productBrand;
    private String productDescription;
    private Double productPrice;
    private Boolean status;
    private String productCategory;
}
