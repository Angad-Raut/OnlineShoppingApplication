package com.projectx.microservice.adminservice.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewDiscountDto {
    private Integer srNo;
    private Long id;
    private Integer discountType;
    private Double discount;
    private String status;
}
