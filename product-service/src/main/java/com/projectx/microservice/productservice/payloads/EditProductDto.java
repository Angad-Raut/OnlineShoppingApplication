package com.projectx.microservice.productservice.payloads;

import com.projectx.microservice.productservice.entity.ProductFeatures;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditProductDto {
    private Long productId;
    private String productName;
    private String productBrand;
    private String productDescription;
    private Double productPrice;
    private String priceCurrency;
    private Long categoryId;
    private String productHsnCode;
    private Integer productTaxPercentage;
    private List<ProductFeatureDto> productFeatures = new ArrayList<>();
    private byte[] productImage;
}
