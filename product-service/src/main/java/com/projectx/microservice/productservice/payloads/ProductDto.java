package com.projectx.microservice.productservice.payloads;

import com.projectx.microservice.productservice.entity.ProductFeatures;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long productId;
    @NotNull(message = "{product.name.not.null}")
    private String productName;
    @NotNull(message = "{product.brand.not.null}")
    private String productBrand;
    @NotNull(message = "{product.description.not.null}")
    private String productDescription;
    @NotNull(message = "{product.price.not.null}")
    private Double productPrice;
    private String priceCurrency;
    @NotNull(message = "{product.categoryid.not.null}")
    private Long categoryId;
    @NotNull(message = "{product.hsnCode.not.null}")
    private String productHsnCode;
    @NotNull(message = "{product.taxPercentage.not.null}")
    private Integer productTaxPercentage;
    @NotNull(message = "{product.image.not.null}")
    private MultipartFile productImage;
    @NotEmpty(message = "${product.features.empty}")
    private List<ProductFeatureDto> productFeatures;
}
