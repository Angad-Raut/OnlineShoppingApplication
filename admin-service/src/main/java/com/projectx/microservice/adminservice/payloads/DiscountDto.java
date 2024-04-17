package com.projectx.microservice.adminservice.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountDto {
    private Long id;
    @NotNull(message = "{discountType.not.null}")
    private Integer discountType;
    @NotNull(message = "{discount.not.null}")
    private Double discount;
}
