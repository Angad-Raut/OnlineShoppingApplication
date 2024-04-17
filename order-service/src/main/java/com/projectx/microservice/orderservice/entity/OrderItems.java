package com.projectx.microservice.orderservice.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class OrderItems {
    private Long productId;
    private Double productPrice;
    private Integer productQuantity;
    private Double totalPrice;
    private Integer cgstPercentage;
    private Integer igstPercentage;
    private Integer sgstPercentage;
    private Double cgstTaxAmount;
    private Double igstTaxAmount;
    private Double sgstTaxAmount;
}
