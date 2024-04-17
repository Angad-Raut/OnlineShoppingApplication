package com.projectx.microservice.orderservice.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {
    @NotNull(message = "{productId.not.null}")
    private Long productId;
    @NotNull(message = "{product.price.null}")
    private Double productPrice;
    @NotNull(message = "{productQuantity.not.null}")
    private Integer productQuantity;
}
