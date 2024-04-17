package com.projectx.microservice.productservice.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductFeatureDto {
    @NotNull(message = "${product.features.name}")
    private String featureName;
    @NotNull(message = "${product.features.value}")
    private String featureValue;
}
